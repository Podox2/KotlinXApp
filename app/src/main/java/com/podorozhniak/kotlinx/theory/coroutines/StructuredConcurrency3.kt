package com.podorozhniak.kotlinx.theory.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

/*
Чому не можна "ковтати" CancellationException

1. Це сигнал, а не помилка
CancellationException — не показник збою, а механізм кооперативної скасування.
Coroutine framework використовує саме цей exception, щоб позначити corutine/job як Cancelled.
Якщо ви його перехоплюєте в catch (e: Exception) і не прокидуєте далі — з точки зору батьківського Job усе виглядає так,
ніби corutine успішно завершилась (Completed), хоча насправді мало відбутися скасування.
2. Ламається structured concurrency
У structured concurrency скасування батьківського Job має каскадно скасувати всі дочірні coroutines,
і навпаки — незловлений CancellationException в дочірній corutine сигналізує батьку, що можна завершуватись.
Якщо exception "з'їдений" десь всередині, батьківський scope може зависнути в очікуванні дочірньої corutine,
яка насправді вже логічно мертва, або просто неправильно визначить фінальний стан дерева задач.
3. ensureActive() / кооперативність перестає працювати
Скасування в Kotlin coroutines кооперативне: воно позначає Job як cancelled,
а сам код повинен або сам перевіряти isActive, або природно кинути CancellationException
(наприклад, з delay(), yield(), будь-якої suspend-функції). Якщо ви ловите цей exception і просто логуєте/ігноруєте,
а потім продовжуєте виконання — ви фактично обходите механізм скасування, і corutine продовжує робити роботу,
яку мала припинити (витік ресурсів, зайві запити в мережу, робота "в порожнечу").
4. finally — можна, catch без rethrow — ні
Правильний патерн:
* */
fun testException() {
    try {
        CoroutineScope(Dispatchers.IO).launch {
            // doSuspendingWork()
        }
    } catch (e: CancellationException) {
        // cleanup()
        throw e // обов'язково прокинути далі
    } finally {
        // теж ок для очищення ресурсів
    }
}

/*
Тобто CancellationException можна перехопити для cleanup-логіки (закрити файл, відмінити мережевий запит тощо),
але після цього його треба обов'язково throw знову.

5. try/catch (e: Exception) — небезпечний паттерн
Якщо ловити базовий Exception без явної перевірки на CancellationException, ви автоматично й
непомітно ловите і скасування теж. У Kotlin CancellationException — підклас Exception (не Error),
тому загальний catch блок його теж перехопить, якщо ви явно не виключите цей кейс.

Підсумок: прокидування CancellationException вгору — це те, як coroutine framework дізнається,
що завдання дійсно скасовано, і може коректно завершити Job, скасувати дочірні задачі та звільнити ресурси.
Проковтнути його — означає зламати весь механізм кооперативного скасування.
* */
package com.podorozhniak.kotlinx.practice.view.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.FragmentCoroutinesBinding
import com.podorozhniak.kotlinx.practice.base.BaseFragment
import com.podorozhniak.kotlinx.practice.di.appContext
import com.podorozhniak.kotlinx.practice.extensions.onClick
import com.podorozhniak.kotlinx.practice.view.MainActivity
import com.podorozhniak.kotlinx.practice.view.fragment_result_api.SecondActivity
import com.podorozhniak.kotlinx.theory.coroutines.handler
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class CoroutineFragment : BaseFragment<FragmentCoroutinesBinding>() {

    private val COR_DEBUG = "coroutine debug:"
    // вид джобів, які дозволяють перевіряти чи джоба виконана
    private var completableJob: CompletableJob? = null

    override val layoutId: Int
        get() = R.layout.fragment_coroutines

    override fun onCreateViewBinding(view: View) = FragmentCoroutinesBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnCallsByLaunch.onClick {
                fakeApiCallsByLaunch()
            }
            btnCallsByAsyncAwait.onClick {
                fakeApiCallsByAsyncAwait()
            }
            btnCallsSequential.onClick {
                fakeApiCallsSequential()
            }
            btnSimpleSequential.onClick {
                simpleSequential()
            }
            btnRunBlocking.onClick {
                runBlockingExample()
            }
            btnGlobalScope.onClick {
                globalScopeIssue()
            }
            btnGlobalScopeSecond.onClick {
                globalScopeIssueAnotherExample()
            }
            btnHandler.onClick {
                handlerApproach()
            }
            btnNetworkRequests.onClick {
                findNavController().navigate(CoroutineFragmentDirections.openNetworkRequestFragment())
            }
        }
    }

    /*
    * Кожна корутина виконується в якомусь контексті. Цей контекст представляється класом CoroutineContext.
    * Цей контексе - це набір параметрів (джоба, диспатчер, ексепшн хендлер).
    * Можна додавати контексти (cntxt+cntxt), можна видаляти або змінювати елементи в ньому (наприклад, диспатчер через witchContext()).
    * Job - об'єкт задачі, яка виконуєтсья у фоні. За допомогою джоби можна керувати роботою корутини,
    * джобу можна відмінити, вона має свій життєвий цикл і на основі її можна створити ієрархію батько - дитина.
    *
    *
    * ЖЦ джоби:
    * new -> active -> completing -> completed
    *          |___________|
    *               |
    *          cancelling-> cancelled
    *
    * Dispatchers (відповідають за потоки):
    *   Main - головний потік; в Андроїд - UI потік
    *   Default - для інтенсивної обчислювальної роботи
    *   IO - для I/O операцій
    *   Unconfined - не прив'язаний до конкретного потоку. Виконання корутин відбувається в тому ж
    *                потоці, в якому корутина була створена і запущена.
    *
    *
    * CoroutineScope - життєвий цикл для виконання асинхронних операцій. Відповідає за свої дочірні корутини.
    * Всі корутини мають бути прив'язані до скоупу. Виключення - білдер runBlocking { }.
    * runBlocking - блокує потік, поки корутина не виконає роботу, тому і не потрібний скоуп.
    *
    * Structured concurrency - механізм, який надає ієрархічну структуру для організації роботи корутин.
    * По суті всі принципи SC будуються на основі CoroutineScope. А під капотом через відношення батько-дитина в джоб.
    * Принципи роботи CoroutineScope:
    * 1. Відміна скоуп - відміна корутин
    * 2. Скоуп знає про всі корутини (зберігає ссилки на них, які запущені в рамках нього)
    * 3. Скоуп очікує на виконання всіх дочірніх корутин (успішно або з помилкою), але не обов'язково завершується разом з ними.
    *
    * Scope vs Context
    * якщо глянути на реалізацію CoroutineScope, то можна побачити, що це лише обгортка над контекстом (має одне поле coroutineContext)
    * Головна різниця між ними - їхнє цільове призначення.
    * Context - набір параметрів для виконання корутини; Scope - призначений для об'єднання корутин, запущених в рамках нього
    * і надає спільну батьківську джобу для цих корутин.
    *
    * GlobalScope - живе поки живе процес
    */

    // custom scope
    private val uiJob = SupervisorJob()
    //Scope в якому працює курутина, може задаватись елементами як контекст, джоба, хендлер
    private val uiScope = CoroutineScope(context = Dispatchers.Default + uiJob + handler)
    private fun executeInCustomScope() {
        // можна задавати ім'я корутині
        uiScope.launch {
            Log.e(COR_DEBUG,"uiScope")
        }
    }
    // !! треба самому відміняти скоуп, якщо він кастомний (відміняться всі запущені корутини в рамках нього)
    override fun onDestroyView() {
        super.onDestroyView()
        uiScope.cancel()
    }

    // функція coroutineScope { } використовується, коли в suspend fun треба запустити корутину
    private suspend fun anotherWayToCreateScope() {
        coroutineScope {
            // паралельні операції
            launch {

            }
            launch {
                try {
                } catch (e: Exception) {
                } finally {
                    // NonCancellable - спеціальна версія джоби, яку не можна відмінити
                    // якщо потрібно обов'язково щось виконати, навіть якщо відбулась помилка
                    // правильно використовувати тільки з withContext { }
                    withContext(NonCancellable) {
                    }
                }
            }
        }
    }

    // паралельне виконання через launch
    private fun fakeApiCallsByLaunch() {
        val startTime = System.currentTimeMillis().toInt()
        // запускаємо батьківську корутину, щоб в принципі викликати suspend функції
        val parentJob = lifecycleScope.launch {
            // переключаємось на IO потік (в цьому прикладі це не важливо, але в цілому потрібно)
            withContext(Dispatchers.IO) {
                // запускаємо першу дочірню корутину для першого реквесту
                val job1 = launch {
                    val time = measureTimeMillis {
                        Log.e(COR_DEBUG, "launching job1 in ${getThreadName()}")
                        val response1 = fakeRequest1()
                        Log.e(COR_DEBUG, response1)
                    }
                    Log.e(COR_DEBUG, "job1 time - $time ms")
                }

                // запускаємо другу дочірню корутину для другого реквесту.
                // вона буде виконуватись паралельно першій корутині
                val job2 = launch {
                    val time2 = measureTimeMillis {
                        Log.e(COR_DEBUG, "launching job2 in ${getThreadName()}")
                        val response2 = fakeRequest2()
                        Log.e(COR_DEBUG, response2)
                    }
                    Log.e(COR_DEBUG, "job2 time - $time2 ms")
                }
            }
        }
        // загальний час виконання ~= роботі довшого (другого) реквесту
        // бо реквести виконались паралельно
        parentJob.invokeOnCompletion {
            val totalTime = System.currentTimeMillis().toInt() - startTime
            Log.e(COR_DEBUG, "total $totalTime ms")
        }
    }

    // паралельне виконання через async / await
    // всі коментарі зверху правдиві і тут
    private fun fakeApiCallsByAsyncAwait() {
        val startTime = System.currentTimeMillis().toInt()
        val parentJob = lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                // запускаємо першу корутину для першого реквесту
                val job1: Deferred<String> = async {
                    Log.e(COR_DEBUG, "launching job1 in ${getThreadName()}")
                    fakeRequest1()
                }

                val job2: String = async {
                    Log.e(COR_DEBUG, "launching job2 in ${getThreadName()}")
                    fakeRequest2()
                }.await()

                Log.e(COR_DEBUG, job1.await())
                Log.e(COR_DEBUG, job2)
            }
        }
        // загальний час виконання ~= роботі довшого (другого) реквесту
        // бо реквести виконались паралельно
        parentJob.invokeOnCompletion {
            val totalTime = System.currentTimeMillis().toInt() - startTime
            Log.e(COR_DEBUG, "total $totalTime ms")
        }
    }

    // послідовне виконання через async / await
    // всі коментарі зверху правдиві і тут
    private fun fakeApiCallsSequential() {
        val startTime = System.currentTimeMillis().toInt()
        val parentJob = lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                // запускаємо першу корутину для першого реквесту
                // job1 - Deferred<String>
                val job1: Deferred<String> = async {
                    Log.e(COR_DEBUG, "launching job1 in ${getThreadName()}")
                    fakeRequest1()
                }

                val job2: String = async {
                    Log.e(COR_DEBUG, "launching job2 in ${getThreadName()}")
                    // очікуємо виконання job1, щоб використати результат
                    fakeRequest3(job1.await())
                }.await()
                Log.e(COR_DEBUG, job2)

                /*
                // інший варіант - await() в кінці лямбди
                // job3 - стрінга
                val job3: String = async {
                    Log.e("$COR_DEBUG launching job1 in ${getThreadName()}")
                    fakeRequest1()
                }.await()

                val job4: String = async {
                    Log.e("$COR_DEBUG launching job2 in ${getThreadName()}")
                    // очікуємо виконання job1, щоб використати результат
                    fakeRequest3(job3)
                }.await()
                Log.e("$COR_DEBUG $job4")
                 */
            }
        }
        // загальний час виконання ~= сумарно роботі двох реквестів
        // бо реквести виконались послідовно один за одним
        parentJob.invokeOnCompletion {
            val totalTime = System.currentTimeMillis().toInt() - startTime
            Log.e(COR_DEBUG, "total $totalTime ms")
        }
    }

    // взагалі якщо потрібно щось виконати послідовно, то можна це робити в одній корутині (що логічно)
    private fun simpleSequential() {
        val startTime = System.currentTimeMillis().toInt()
        val parentJob = lifecycleScope.launch {
            val response = fakeRequest1()
            val response3 = fakeRequest3(response)
            Log.e(COR_DEBUG, response3)
        }
        parentJob.invokeOnCompletion {
            val totalTime = System.currentTimeMillis().toInt() - startTime
            Log.e(COR_DEBUG, "total $totalTime ms")
        }
    }

    // runBlocking { } блокує потік, в якому викликається
    // перший fakeRequest2() встигає спрацювати (бо в нього ділей 1 секунда)
    // далі потік блокується на 3 секунди, після розблокування виконуються наступні 2 реквести
    private fun runBlockingExample() {
        lifecycleScope.launch {
            Log.e(COR_DEBUG, fakeRequest2())
            Log.e(COR_DEBUG, fakeRequest2())
            Log.e(COR_DEBUG, fakeRequest2())
        }

        lifecycleScope.launch {
            // ділей 1.5 секунди - більше ніж ділей першого реквесту
            delay(1_500)
            runBlocking {
                Log.e(COR_DEBUG, "thread is blocked")
                delay(3_000)
                Log.e(COR_DEBUG, "thread is unblocked")
            }
        }
    }

    private fun blockThreadExamples() {
        //не заблокує потік
        GlobalScope.launch(Dispatchers.Main) {
            delay(5_000L)
        }
        // заблокує потік
        runBlocking {
            launch {
                delay(5_000L)
            }
        }
        //варіант без корутин
        Thread.sleep(5_000L)
    }

    // при використанні GlobalScope не дотримується structured concurrency
    // globalJob (~313 рядок) виступає як парент для двох дочірніх джоб
    // але він нічого не знає про них, тому відразу буде виконаний invokeOnCompletion
    // життєвий цикл джобів запущений в GlobalScope неможливо відслідкувати
    private fun globalScopeIssue() {
        val startTime = System.currentTimeMillis().toInt()
        // нормальний скоуп
        val parentJob = lifecycleScope.launch {
            launch {
                Log.e(COR_DEBUG, "correct job - ${fakeRequest1()}")
            }
            launch {
                // вивід не відбудеться, адже джоба відміниться, тому що батьківська корутина відмінить себе і дочірні джоби
                Log.e(COR_DEBUG, fakeRequest2())
            }
        }
        // відміняємо парент джоб, щоб відмінити дочірні джоби, які ще активні
        lifecycleScope.launch {
            delay(500)
            // друга дочірня корутина відміниться
            parentJob.cancel()
        }

        val globalJob = lifecycleScope.launch {
            GlobalScope.launch {
                Log.e(COR_DEBUG, "global job - ${fakeRequest1()}")
            }
            GlobalScope.launch {
                // вивід відбудеться, батьківська корутина не відмінить дочірні джоби
                Log.e(COR_DEBUG, "global job - ${fakeRequest2()}")
            }
        }
        // намагаємось відмінити парент джобу, щоб відмінити дочірні джоби, які ще активні (але вони не відміняться)
        lifecycleScope.launch {
            delay(500)
            // батьківська джоба не зможе відмінити дочірні
            globalJob.cancel()
        }

        // виконається правильно - після виконання (або відміни) двох дочірніх джоб
        parentJob.invokeOnCompletion {
            val totalTime = System.currentTimeMillis().toInt() - startTime
            Log.e(COR_DEBUG, "correct job total $totalTime ms")
        }
        // виконається миттєво при створенні батьківської джоби, бо дочірні джоби в глобал скоупі
        globalJob.invokeOnCompletion {
            val totalTime = System.currentTimeMillis().toInt() - startTime
            Log.e(COR_DEBUG, "global job total $totalTime ms")
        }
    }

    //курутина запущена в глобал скоупі, навіть після знищення актівіті, буде жива
    private fun globalScopeIssueAnotherExample() {
        // ця корутина не буде знищена після закриття актівіті
        // що логічно, бо GlobalScope != lifecycleScope актівіті (або якогось view)
        // GlobalScope прив'язаний до ЖЦ аплікухи / процесу
        GlobalScope.launch {
            while (true) {
                Log.e(COR_DEBUG, "GlobalScope coroutine still alive!")
                delay(1_000L)
            }
        }
        // ця корутина буде знищена після закриття актівіті
        lifecycleScope.launch {
            while (true) {
                Log.e(COR_DEBUG, "lifecycleScope coroutine still alive!")
                delay(1_000L)
            }
        }

        lifecycleScope.launch {
            delay(3_000L)
            startActivity(Intent(appContext, SecondActivity::class.java))
            delay(2_000L)
            Log.e(COR_DEBUG, "closing activity")
            (requireActivity() as MainActivity).finish()
        }
    }

    // якщо треба оновити змінну, яка знаходиться поза скоупом корутини при використанні launch
    // треба викликати join(), щоб дочекатись значення
    // UPD. 24.04.2024 щось не працює
    private fun joinExample() {
        var someString = ""
        var someStringJoin = ""
         lifecycleScope.launch {
             val job = launch {
                 someString = fakeRequest1()
                 someStringJoin = fakeRequest1()
             }
             job.join()
        }
        Log.e(COR_DEBUG, "someString = $someString")
        Log.e(COR_DEBUG, "someStringJoin = $someStringJoin")
    }

    //джоба зупиниться після 3 секунд
    private fun jobWithTimeOut() {
        val jobTimeOut = lifecycleScope.launch {
            withTimeout(3_000L) {
                repeat(5) {
                    log("Coroutine is still working $it")
                    delay(1500L)
                }
            }
        }
    }

    // код з StructuredConcurrency2.kt
    // аплікуха не крешнеться
    // друга дочірня і батьківська джоби зафейляться
    private fun handlerApproach() {
        //val parentJobWithExcHandler = CoroutineScope(Dispatchers.Default + handler).launch {
        // код змінений на більш для фрагменту
        val parentJobWithExcHandler = lifecycleScope.launch(handler) {
            launch {
                delay(400)
                throw Exception("some exc")
            }
            launch {
                delay(1000)
                // не виведиться, бо джоба зафейлиться
                Log.e(COR_DEBUG, "Second job is finished")
            }
        }
        parentJobWithExcHandler.invokeOnCompletion { throwable ->
            if (throwable != null) {
                // виведиться "some exc"
                Log.e(COR_DEBUG, "${throwable.message}")
            } else {
                // не виведиться, бо джоба зафейлиться
                Log.e(COR_DEBUG, "nice")
            }
        }
    }

    private fun getThreadName() = Thread.currentThread().name

    private suspend fun fakeRequest1(): String {
        delay(300)
        return "Fake response 1"
    }

    private suspend fun fakeRequest2(): String {
        delay(1_000)
        return "Fake response 2"
    }

    private suspend fun fakeRequest3(param: String): String {
        delay(1_000)
        return "Fake response 2"
    }
}
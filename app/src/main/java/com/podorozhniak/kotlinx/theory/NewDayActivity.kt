package com.podorozhniak.kotlinx.theory

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.practice.extensions.joinToString
import com.podorozhniak.kotlinx.practice.extensions.lastChar
import com.podorozhniak.kotlinx.theory.oop.Person
import kotlin.properties.Delegates

/*          Access
*   Java                Kotlin
*   public              public
*   package             internal
*   protected           protected
*   private             private
* */


//public по замовчуванню
//Any/Any? = Object
//Unit = Void
//Nothing функції не верне управління (коли функція просто викидає помилку)
//в Kotlin немає простих типів тільки ссилочні. при переводі коду в байт-код JVM, при можливості ці типи
//переводяться в прості (примітивні)

//використання рядкового шаблону ($name)
fun sayHello(name: String) {
    println("Hello, $name /$")
}

//if виступає в якості тернарного опаратора
//if вираз, а не інструкція
//функція із тіло-блоком
fun max(a: Int, b: Int): Int {
    return if (a > b) a else b
}

//метод зверху можна переписати, тому що тіло складається з одного виразу.
//тип значення, що вертається можна не вказувати. компілятор сам його може визначити, це називається
//виведення типу (type inference). це відноситься тільки до функцій із тілом-виразом
fun maxShort(a: Int, b: Int) = if (a > b) a else b

//when заміна switch/case
//when теж вираз
//break не потрібно писати
fun getDay(day: Int) = when (day) {
    1, 3, 5, 7 -> "Monday"
    2, 4, 6 -> "Tuesday"
    else -> "Number is greater then 7"
}

fun fizzBuzz(i: Int) = when {
    i % 15 == 0 -> "FizzBuzz"
    i % 5 == 0 -> "Buzz"
    i % 3 == 0 -> "Fizz"
    else -> "$i"
}

fun funWithManyParameters(
    firstInt: Int,
    secondInt: Int,
    firstString: String,
    secondString: String
) {
}

fun funWithManyParametersButDefaultToo(
    firstInt: Int,
    secondInt: Int,
    firstString: String = "f",
    secondString: String = "cx"
) {
}

//коснтанта часу компіляції (значення присвоюється під час компіляції, а не під
//час виконання програми. її неможливо змінити
const val NUMBER: Int = 5000

/*extension ф-ція скомпілюється в ф-цію, в яку передається об'єкт (з типом String в даному випадку),
 ну і для цього об'єкту не можна достукатись до private полів/методів
* */
fun String.extensionExample() {
    this.plus(" some string")
}

class NewDayActivity : AppCompatActivity() {
    //заміна статики в котліні
    //companion - внутрішній static (при переведенні в байт-код?)
    companion object {
        const val TAG_ACTIVITY = "NewDayActivity"
    }

    //можна не вказувати тип
    //val = final у Java (не можна змінити)
    //var звичайне посилання у Java (можна змінити)
    //бажано оголошувати всі змінні val, це наблизить код до функціонального стилю
    val counter = 0
    val counter2: Int = 0

    /* Обычно, свойства, объявленные non-null типом, должны быть проинициализированы в конструкторе,
    но иногда переменную нельзя сразу инициализировать (view ініціалізуються в onCreate()), сделать это можно чуть позже.
    Для таких случаев придумали новый модификатор lateinit (отложенная инициализация).
    Переменная обязательно должна быть изменяемой (var). Не должна относиться к примитивным
    типам (Int, Double, Float и т.д). Не должна иметь собственных геттеров/сеттеров. Подобный
    подход удобен во многих случаях, избегая проверки на null. В противном случае пришлось
    бы постоянно использовать проверку или утверждение !!, что засоряет код. Если вы обратитесь
    к переменной до её инициализации, то получите исключение "lateinit property ... hos not been
    initialized" вместо NullPointerException.*/
    lateinit var textView: TextView

    /*simply allows a property to be initialized at a later time. notNull() is similar to lateinit.
    In most cases, lateinit is preferred since notNull() creates an extra object for each property.
    However, you can use notNull() with primitive types, which lateinit doesn’t support.*/
    val fullname: String by Delegates.notNull<String>()

    /*lazy() это функция, которая принимает лямбду и возвращает экземпляр класса Lazy<T>,
    который служит делегатом для реализации ленивого свойства: первый вызов get()
    запускает лямбда-выражение, переданное lazy() в качестве аргумента, и запоминает
    полученное значение, а последующие вызовы просто возвращают вычисленное значение.
    По умолчанию вычисление ленивых свойств синхронизировано: значение вычисляется только
    в одном потоке выполнения, и все остальные потоки могут видеть одно и то же значение.
    Если синхронизация не требуется, передайте LazyThreadSafetyMode.PUBLICATION
    в качестве параметра в функцию lazy(), тогда несколько потоков смогут исполнять
    вычисление одновременно. Или если вы уверены, что инициализация всегда будет происходить
    в одном потоке исполнения, вы можете использовать режим LazyThreadSafetyMode.NONE,
    который не гарантирует никакой потокобезопасности.*/
    private val lazyValue: String by lazy(LazyThreadSafetyMode.NONE) {
        println("computed!")
        "Hello"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_day)
        sayHello("World")
        println(lazyValue)

        //конструктор викликається без ключового слова new
        val person1 = Person("Rus", false)
        //доступ до властивості напряму, а не через метод читання
        println(person1.name)

        //for немає, є тільки foreach
        val diapason = 1..10
        for (i in diapason) {
            print(fizzBuzz(i))
        }
        /* 1..10
           1 until 10
           10.downTo(1)
           1..10 step 2*/

        //Check if the value is in a range
        if(2 in diapason){}

        //рандомить числов від до 100 включно
        val randomNumber = (1..100).shuffled().last()

        //з кінця через 2 (тільки парні числа)
        for (i in 100 downTo 1 step 2) {
            print(fizzBuzz(i))
        }

        //обробка помилок в цілому як у Java

        //is аналог instanceof в Java
        //автоматичне приведення типу
        //після перевірки тип не потрібно переводити в інший
        val num = 10
        if (num is Int) {
            println("$num")
        }

        //для приведення типу використовується ключове слово as
        // val n = e as Num

        //коли при виклику функції не ясно за що відповідають параметри
        //можна використовувати іменовані параметри
        funWithManyParameters(1, 2, "a", "b")
        funWithManyParameters(firstInt = 1, secondInt = 2, firstString = "a", secondString = "b")

        //static функція із іншого файлу
        joinToString("ss11")

        //виклик функції-розширення
        //"Kotlin" - об'єкт отримувач
        println("Kotlin".lastChar())

        //vararg ключове слово для довільної кількості аргументів функції

        //не придумав адекватного прикладу
        2 to infixChangeToNum(3)

        //NULL
        //розділення типів на nullable і non-nullable дозволяє ще на моменті
        //компіляції показати помилки
        val string: String = ""
        val stringNull: String? = null

        //не можна викликати функцію, треба не null тип
        //strLen(stringNull)

        //оператора as? перевіряє об'єкт на тип, і якщо вони не рівні присвоює їй null
        // val n = e as? Num

        //оператор let {}
        //null змінну, передаємо в функцію, де вона має бути не null
        // так компілятор вкаже помилку
        // strLen(stringNull)
        stringNull?.let {
            //повний варіант
            //stringNull -> strLen(stringNull)
            //короткий варіант
            strLen(it)
        }

        //приведення типів
        val num2: Long = 1
        1.toString()
        "1".toLong()

        //робота зі списками
        val names = listOf("Eli", "Mordoc", "Sophie")
        val namesMutable = mutableListOf("Eli", "Mordoc", "Sophie")

        val second = names[1]
        val firstName = names.first()
        val lastName = names.last()
        //якщо індекс завеликий, використати лямбду, яка вертає рядок
        val getElse = names.getOrElse(4) { "Index too big" }
        val getNull = names.getOrNull(4)
        val getNullOrDefault = names.getOrNull(4) ?: "Index too big"
        val contains = names.contains("Eli")
        val containsMany = names.containsAll(listOf("Eli", "Mordoc"))
        val randomName = names.shuffled().first()

        namesMutable.add("213")
        val newMutList = names.toMutableList()
        val newList = namesMutable.toList()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            namesMutable.removeIf { it.contains('o') }
        }

        //пройтись по елементам списку можна різними способами
        for (name in names) {
            println(name)
        }
        names.forEach {
            println(it)
        }
        names.forEachIndexed { index, item ->
            println("$index. $item")
        }

        (0..9).forEach { }

        //видалити дублікати зі списку (зробити зі списку множину),
        //але мати доступ до елементів через індекс (вернутись до списку)
        val uniqueNames = names.toSet().toList()
        val uniqueNamesLaconic = names.distinct()

        //список примітивних значень, а не ссилок на об'єкти
        //в основному треба для взаємодії із джава кодом
        val intArray = intArrayOf(1, 2, 3)
    }


    //оголошення інфіксної функції
    infix fun infixChangeToNum(num: Int): Int = num

    //функція, в яку не можна передати null-тип
    fun strLen(s: String): Int = s.length


    //функція, в яку  можна передати null-тип
    //оператор ?. -> if(s != null) s.length else null
    //тобто перевіряє на null, якщо не null, то верне значення, якщо null, верне null
    //тому функція має вертати null-тип (Int?)
    fun strLenNull(s: String?): Int? = s?.length

    //опаратор елвіс ?:
    //якщо s не null, вертає його, якщо null - значення після оператора
    fun foo(s: String?) {
        val t = s ?: ""
    }

    //використання двох операторів
    fun strLenElvis(s: String?): Int = s?.length ?: 0

    //опаратор !!
    //використовується при впевненості, щоб об'єкт точно не null
    //якщо об'єкт буде  null викинеться виключення NullPointerException
    fun strLenN(s: String?): Int = s!!.length


    //створює інтент для надсилання листа через електронну пошту
    fun sendEmailToSupport() {
        val mailto = getString(R.string.support_email) +
                "?&subject=" + Uri.encode(getString(R.string.email_subject)) +
                "&body=" + Uri.encode(getString(R.string.email_body))

        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse(mailto)

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(intent, getString(R.string.send_email)))
        }
    }


}

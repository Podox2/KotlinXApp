package com.podorozhniak.kotlinx.practice.extensions

import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import com.podorozhniak.kotlinx.R

//функції можна прописувати не в класі
//вони замінюють static функції
fun joinToString(s: String): String{
    return "ss"
}

//функція-розширення для класу String
//String - тип-отримувач (receiver type)
//this - об'єкт-отримувач (receiver object)
//fun String.lastChar(): Char = this.get(this.length - 1)
//можна опустити this
/*
extension функція скомпілиться в функцію, в яку передається об'єкт,
ну і для цього об'єкту не можна достукатись до private полів
* */
fun String.lastChar(): Char = get(length - 1)

fun String.isMailValid() = android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isPhoneNumberValid() = "^(0[4-9][0-9]\\d{7})$".toRegex().matches(this)

//Highlight text. If word begins with '#' or '@', method will highlight it
fun showHighlightText(view: TextView, text: String?) {
    val stringList = text?.split(" ") ?: listOf()
    val newString = SpannableStringBuilder()
    stringList.forEach {
        val string = SpannableString(it)
        if (it[0] == '#' || it[0] == '@') {
            string.setSpan(
                ForegroundColorSpan(view.context.getColor(R.color.blue)),
                0,
                it.length,
                0
            )
        }
        newString.append(string).append(" ")
    }
    newString.trim()
    view.text = newString
}

//add spaces
fun phoneFormatter(view: TextView, text: String?) {
    view.text = if (text?.isBlank()?.not() == true) {
        "+38" + StringBuilder(text)
            .insert(1, " ")
            .insert(4, " ")
            .insert(8, " ")
            .insert(11, " ")
    } else
        text
}

/*
Необходимо реализовать метод truncate усекающий исходную
строку до указанного числа символов и добавляющий заполнитель "..." в конец строки
 */
fun String.truncate(index: Int = 16): String {
    if (this.length < index) {
        return this
    }
    var string = this.substring(0, index)
    while (string[string.length - 1] == ' ') {
        string = string.substring(0, string.length - 1)
    }
    string = "$string..."
    return string
}

/*
Необходимо реализовать метод stripHtml для очистки строки от лишних пробелов,
html тегов, escape последовательностей
 */
fun String.stripHtml() = this.replace("<[^>]*>".toRegex(), "").replace("\\s+".toRegex(), " ").trim()

/*
перевірка чи перший символ рядку велика літера
 */
fun String.isUpper(): Boolean {
    val pattern = "[A-Z[А-Я]]".toRegex()
    return (pattern.matches(this))
}

/*
перевірка чи перший символ рядку маленька літера
 */
fun String.isLower(): Boolean {
    val pattern = "[a-z[а-я]]".toRegex()
    return (pattern.matches(this))
}

/*
перевірка чи в рядку немає цифр
 */
fun String.isNoDigits(): Boolean{
    val pattern = """([^0-9]*)""".toRegex()
    return pattern.matches(this)
}

/*
перевірка чи в рядку тільки цифри
 */
fun String.isOnlyDigits(): Boolean{
    val pattern = """([0-9]*)""".toRegex()
    return pattern.matches(this)
}

fun String.isLetter() = "^[a-zA-Z]$".toRegex().matches(this)



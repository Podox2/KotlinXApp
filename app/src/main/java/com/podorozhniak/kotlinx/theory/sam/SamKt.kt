package com.podorozhniak.kotlinx.theory.sam

// щоб інтерфейс був Single Abstract Method-інтерфейсом, в нього має бути один метод
// і він має бути поміченим ключовим словом fun (а в Java ніяких ключових слів не потрібно)
fun interface SamKt {
    fun saySomething(): String
}
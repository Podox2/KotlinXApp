package com.podorozhniak.kotlinx.theory.java;

import kotlin.jvm.Volatile;

public class JavaFeatures {

    //volatile - брати зміну з головної копії, а не з кеша потоку
    /*Это означает, что значение переменной будет "всегда читаться".
    Например, в многопоточных приложениях один поток прочёл значение a=1,
    передал управление другому потоку, который изменил значение на a=2,
    потом управление вернулось. Так вот, без volatile значение a у первого
    потока будет 1, т.к. первый поток "помнит", что a=1, с volatile - 2, т.к.
    первый поток снова прочтет значение и получит уже измененное.*/
    @Volatile
    String s = "s";

    void javaStrings() {
        String string1 = "string1";
        String string2 = "string1"; // вернеться ссилка на string1, тому що в пулі стрінгів вже є такий стрінг
        String string3 = new String("string1"); // нова ссилка
    }

    //synchronized - до блоку кода або метода має доступ тільки один потік, інший чекає
    synchronized void javaMultiThreading() {
        synchronized (this) {
            System.out.println("synchronized");
        }
    }
}

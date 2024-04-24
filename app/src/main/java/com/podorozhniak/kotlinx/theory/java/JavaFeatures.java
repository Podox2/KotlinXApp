package com.podorozhniak.kotlinx.theory.java;

import kotlin.jvm.Volatile;

public class JavaFeatures {

    //volatile - брати зміну з головної копії, а не з кеша потоку
    /*Это означает, что значение переменной будет "всегда читаться".
    Например, в многопоточных приложениях один поток прочёл значение a=1,
    передал управление другому потоку, который изменил значение на a=2,
    потом управление вернулось. Так вот, без volatile значение a у первого
    потока будет 1, т.к. первый поток "помнит", что a=1, с volatile - 2, т.к.
    первый поток снова прочтет значение и получит уже измененное.
    happens before записи перед чтением*/
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

    void testNonInterop() {
        JvmNonInterop jvmNonInterop = new JvmNonInterop("name");
        jvmNonInterop.setName("new name");
        jvmNonInterop.setAge(22);
        int number = JvmNonInterop.Companion.getNumber();
        String s = JvmNonInterop.Companion.getNONE();
        // const val
        String s2 = JvmNonInterop.CONST_NONE;
        //не можна
        //jvmNonInterop.defaultParam();
        jvmNonInterop.defaultParam(10);
    }

    void testInterop() {
        JvmInterop jvmInterop = new JvmInterop("name");
        //сетити значення можна не через сеттер
        jvmInterop.name = "new name";
        jvmInterop.age = 22;
        //виклик функції як статичної для класу
        int number = JvmInterop.getNumber();
        String s = JvmInterop.NONE;
        String s2 = JvmInterop.CONST_NONE;
        //виклик функції з дефолтними параметрами
        jvmInterop.defaultParam();
    }
}

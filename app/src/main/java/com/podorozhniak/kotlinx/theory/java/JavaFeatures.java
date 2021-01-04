package com.podorozhniak.kotlinx.theory.java;

import kotlin.jvm.Volatile;

public class JavaFeatures {

    //volatile - брати зміну з головної копії, а не з кеша потоку
    @Volatile
    String s = "s";

    void javaStrings(){
        String string1 = "string1";
        String string2 = "string1"; // вернеться ссилка на string1, тому що в пулі стрінгів вже є такий стрінг
        String string3 = new String("string1"); // нова ссилка
    }

    //synchronized - до блоку кода або метода має доступ тільки один потік, інший чекає
    synchronized void javaMultiThreading(){
        synchronized (this){
            System.out.println("synchronized");
        }
    }
}

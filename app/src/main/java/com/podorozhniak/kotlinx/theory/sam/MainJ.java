package com.podorozhniak.kotlinx.theory.sam;

import androidx.annotation.NonNull;

// все можна реалізувати через лямбду
public class MainJ {
    public static void main(String[] args) {
        SamJ samJByAnonClass = new SamJ() {
            @Override
            public String saySomething() {
                return "said something";
            }
        };
        SamJ samJByLambda = () -> "said something";


        SamKt samKt = new SamKt() {
            @NonNull
            @Override
            public String saySomething() {
                return "said something";
            }
        };
        SamKt samKtByLambda = () -> "said something";



        NotSamKt notSamKt = new NotSamKt() {
            @NonNull
            @Override
            public String saySomething() {
                return "said something";
            }
        };
        // основна відмінність.
        // навіть не функціональний Kotlin-інтерфейс можна реалзіувати через лямбду
        NotSamKt notSamKtByLambda = () -> "said something";
    }
}
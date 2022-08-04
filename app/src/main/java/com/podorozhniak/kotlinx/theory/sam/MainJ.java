package com.podorozhniak.kotlinx.theory.sam;

import androidx.annotation.NonNull;

// все можна реалізувати через лямбду
public class MainJ {
    public static void main(String[] args) {
        SamJ samJByAnonClass = new SamJ() {
            @Override
            public String saySomething() {
                return "null";
            }
        };
        SamJ samJByLambda = () -> "null";


        SamKt samKt = new SamKt() {
            @NonNull
            @Override
            public String saySomething() {
                return "null";
            }
        };
        SamKt samKtByLambda = () -> "null";


        NotSamKt notSamKt = new NotSamKt() {
            @NonNull
            @Override
            public String saySomething() {
                return "null";
            }
        };
        NotSamKt notSamKtByLambda = () -> "null";
    }
}
package com.podorozhniak.kotlinx.theory.java;

public class Outer {
    private String privateString = "private";
    static private String staticPrivateString = "static";
    public String publicString = "public";

    class InnerOrNested {
        void test() {
            Outer.this.privateString = "can change";
        }
    }

    static class StaticInner {
        void test() {
            //Outer.this.privateString = "asd";
        }
    }
}

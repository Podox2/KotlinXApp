package com.podorozhniak.kotlinx.theory.java;

public class Outer {
    private String privateString = "private";
    static private String staticPrivateString = "static";
    public String publicString = "public";

    class InnerOrNested {
        void test() {
            Outer.this.privateString = "can access";
            Outer.this.publicString = "can access";
            staticPrivateString = "can access";
        }
    }

    static class StaticInner {
        void test() {
            staticPrivateString = "can access";
            //Outer.this.publicString = "can't";
            //Outer.this.privateString = "can't";
        }
    }
}

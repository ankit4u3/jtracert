package com.google.code.jtracert.samples;

import java.util.ArrayList;

public class SimpleApp1 {

    public static void main(String[] arguments) throws Exception {
        method1();
        method2();
    }

    private static void method1() {
        method2();
        method2();
    }

    private static void method2() {
        new ArrayList<SimpleApp1>();
    }

}
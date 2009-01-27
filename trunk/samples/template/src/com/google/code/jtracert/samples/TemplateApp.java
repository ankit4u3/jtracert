package com.google.code.jtracert.samples;

public class %%MAIN_CLASS%% {

    public static void main(String[] arguments) throws Exception {
        method1();
        method2();
    }

    private static void method1() {
        method2();
        method2();
    }

    private static void method2() {
    }

}

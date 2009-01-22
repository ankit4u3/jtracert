package com.google.code.jtracert.samples;

public class SimpleApp1 {

    public static void main(String[] arguments) throws Exception {
        Thread.sleep(1000);
        System.out.println("main");
        method1();
        method2();
    }

    private static void method1() {
        System.out.println("method1");
        method2();
        method2();
    }

    private static void method2() {
        System.out.println("method2");
    }

}

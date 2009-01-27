package com.google.code.jtracert.samples;

import java.util.List;
import java.util.ArrayList;

public class SimpleApp2 {

    static class SimpleApp2InnerClass1 {

        public void method() {

        }

    }

    private static List<SimpleApp2InnerClass1> innerClassObjects
            = new ArrayList<SimpleApp2InnerClass1>();

    static {
        innerClassObjects.add(new SimpleApp2InnerClass1());
        innerClassObjects.add(new SimpleApp2InnerClass1());
        innerClassObjects.add(new SimpleApp2InnerClass1());
    }

    public static void main(String[] arguments) throws Exception {
        method1();
        method2();

        for (SimpleApp2InnerClass1 innerClassObject : innerClassObjects) {
            innerClassObject.method();
        }

    }

    private static void method1() {
        method2();
        method2();
    }

    private static void method2() {
        com.sun.crypto.provider.SunJCE des = new com.sun.crypto.provider.SunJCE();
    }

}

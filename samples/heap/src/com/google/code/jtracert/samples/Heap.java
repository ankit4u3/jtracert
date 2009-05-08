package com.google.code.jtracert.samples;

public class Heap {

    private static TestObject longLiveObject;

    public static void main(String[] arguments) throws Exception {
        createObjectsMethod();
        runGarbageCollector(1);
    }

    public static void createObjectsMethod() {
        new TestObject();
        new TestObject();
        longLiveObject = new TestObject();
    }

    public static void runGarbageCollector(int n) {
        for (int i = 0; i < n; i++) {
            System.gc();
        }
    }

}

class TestObject {

    private int i;

}
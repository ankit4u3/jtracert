package com.google.code.jtracert.classLoader;

/**
 * @author Dmitry Bedrin
 */
public class ExperimentalClass1 {

    public int increment(int argument) {
        return argument + 1;
    }

    public void emptyMethod() {

    }

    public void methodReturnVoid() {
        return;
    }

    public Object methodReturnObject() {
        return new Object();
    }

    public void methodThrowException() {
        throw new RuntimeException();
    }

    public void methodReThrowException() {
        methodThrowException();
    }

}

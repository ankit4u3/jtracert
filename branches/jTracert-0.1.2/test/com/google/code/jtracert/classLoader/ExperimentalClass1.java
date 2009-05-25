package com.google.code.jtracert.classLoader;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 * @author Dmitry.Bedrin@gmail.com
 */
public class ExperimentalClass1 {

    /**
     *
     * @param argument
     * @return
     */
    public int increment(int argument) {
        return argument + 1;
    }

    /**
     *
     */
    public void emptyMethod() {

    }

    /**
     *
     */
    public void methodReturnVoid() {
    }

    /**
     *
     * @return
     */
    public Object methodReturnObject() {
        return new Object();
    }

    /**
     *
     */
    public void methodThrowException() {
        throw new RuntimeException();
    }

    /**
     * 
     */
    public void methodReThrowException() {
        methodThrowException();
    }

}

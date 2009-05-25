package com.google.code.jtracert.model;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public interface MethodCallVisitor<T> {

    /**
     * @param methodCall
     * @return
     */
    T visit(MethodCall methodCall);

}

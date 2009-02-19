package com.google.code.jtracert.model;

/**
 * @author dmitry.bedrin
 */
public interface MethodCallVisitor<T> {

    T visit(MethodCall methodCall);

}

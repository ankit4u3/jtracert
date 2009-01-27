package com.google.code.jtracert.traceBuilder.impl.graph;

import com.google.code.jtracert.model.MethodCall;

/**
 * @author dmitry.bedrin
 */
public interface MethodCallVisitor<T> {

    T visit(MethodCall methodCall);

}

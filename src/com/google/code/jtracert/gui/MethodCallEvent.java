package com.google.code.jtracert.gui;

import com.google.code.jtracert.model.MethodCall;

import java.util.EventObject;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 * @author Dmitry.Bedrin@gmail.com
 */
public class MethodCallEvent extends EventObject {

    private final MethodCall methodCall;

    /**
     *
     * @param methodCall
     */
    public MethodCallEvent(MethodCall methodCall) {
        super(methodCall);
        this.methodCall = methodCall;
    }

    /**
     *
     * @return
     */
    public MethodCall getMethodCall() {
        return methodCall;
    }
    
}

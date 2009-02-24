package com.google.code.jtracert.gui;

import com.google.code.jtracert.model.MethodCall;

import java.util.EventObject;

public class MethodCallEvent extends EventObject {

    private MethodCall methodCall;

    public MethodCallEvent(MethodCall methodCall) {
        super(methodCall);
        this.methodCall = methodCall;
    }

    public MethodCall getMethodCall() {
        return methodCall;
    }
    
}

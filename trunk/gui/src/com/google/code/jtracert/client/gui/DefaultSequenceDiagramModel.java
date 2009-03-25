package com.google.code.jtracert.client.gui;

import com.google.code.jtracert.client.model.MethodCall;

public class DefaultSequenceDiagramModel implements SequenceDiagramModel {

    private MethodCall rootMethodCall;

    public void setRootMethodCall(MethodCall rootMethodCall) {
        this.rootMethodCall = rootMethodCall;
    }

    public MethodCall getRootMethodCall() {
        return rootMethodCall;
    }
    
}

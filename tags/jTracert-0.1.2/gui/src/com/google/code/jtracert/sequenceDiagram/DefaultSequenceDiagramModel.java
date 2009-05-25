package com.google.code.jtracert.sequenceDiagram;

public class DefaultSequenceDiagramModel implements SequenceDiagramModel {

    private MethodCall rootMethodCall;

    public void setRootMethodCall(MethodCall rootMethodCall) {
        this.rootMethodCall = rootMethodCall;
    }

    public MethodCall getRootMethodCall() {
        return rootMethodCall;
    }
    
}

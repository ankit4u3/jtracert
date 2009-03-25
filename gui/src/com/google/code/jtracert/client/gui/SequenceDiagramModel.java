package com.google.code.jtracert.client.gui;

import com.google.code.jtracert.client.model.MethodCall;

public interface SequenceDiagramModel {

    void setRootMethodCall(MethodCall rootMethodCall);
    MethodCall getRootMethodCall();

}

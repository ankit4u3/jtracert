package com.google.code.jtracert.gui;

import java.util.EventListener;

public interface MethodCallListener extends EventListener {

    void onMethodCall(MethodCallEvent methodCallEvent);

}

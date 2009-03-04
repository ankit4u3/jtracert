package com.google.code.jtracert.gui;

import java.util.EventListener;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 * @author Dmitry.Bedrin@gmail.com
 */
public interface MethodCallListener extends EventListener {

    /**
     *
     * @param methodCallEvent
     */
    void onMethodCall(MethodCallEvent methodCallEvent);

}

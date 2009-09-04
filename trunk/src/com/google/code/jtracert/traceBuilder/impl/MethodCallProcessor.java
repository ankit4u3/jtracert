package com.google.code.jtracert.traceBuilder.impl;

import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.traceBuilder.ConfigurableAnalyzer;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public interface MethodCallProcessor extends ConfigurableAnalyzer {

    /**
     * @param methodCall
     */
    void processMethodCall(MethodCall methodCall);

}

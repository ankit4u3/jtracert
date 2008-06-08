package com.google.code.jtracert.traceBuilder.impl;

import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.traceBuilder.ConfigurableAnalyzer;

/**
 * @author Dmitry Bedrin
 */
public interface MethodCallProcessor extends ConfigurableAnalyzer {

    void processMethodCall(MethodCall methodCall);

}

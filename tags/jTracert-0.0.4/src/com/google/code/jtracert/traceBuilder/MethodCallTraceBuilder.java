package com.google.code.jtracert.traceBuilder;

import com.google.code.jtracert.model.JTracertObjectCompanion;

/**
 * @author Dmitry Bedrin
 */
public interface MethodCallTraceBuilder extends ConfigurableAnalyzer {

    void enter(String className, String methodName, String methodDescriptor, Object object, Object[] arguments/*, JTracertObjectCompanion jTracertObjectCompanion*/);

    void leave();
    void leave(Object returnValue);

    void exception(Throwable e);

}

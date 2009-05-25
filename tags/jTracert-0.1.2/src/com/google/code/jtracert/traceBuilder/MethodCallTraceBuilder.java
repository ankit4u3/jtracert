package com.google.code.jtracert.traceBuilder;

import java.lang.instrument.Instrumentation;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public interface MethodCallTraceBuilder extends ConfigurableAnalyzer {

    /**
     * @param className
     * @param methodName
     * @param methodDescriptor
     * @param object
     * @param arguments
     */
    void enter(String className, String methodName, String methodDescriptor, Object object, Object[] arguments);

    /**
     *
     */
    void leave();

    /**
     * @param returnValue
     */
    void leave(Object returnValue);

    /**
     * @param e
     */
    void exception(Throwable e);

    /**
     * @param methodDescriptor
     */
    void leaveConstructor(String methodDescriptor);

    /**
     * @param className
     * @param methodName
     * @param methodDescriptor
     * @param exception
     */
    void leaveConstructor(String className, String methodName, String methodDescriptor, Throwable exception);

    /**
     * @param className
     * @param methodDescriptor
     */
    void preEnterConstructor(String className, String methodDescriptor);

    Instrumentation getInstrumentation();

    void setInstrumentation(Instrumentation instrumentation);

}

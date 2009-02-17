package com.google.code.jtracert.trace;

public interface TraceBuilder {

    void onMethodEnter(Class clazz, Object object, String methodName, String methodDescriptor);

    void onMethodEnter(Class clazz, Object object, String methodName, String methodDescriptor, Object[] methodArguments);

    void onMethodReturn();

    void onMethodReturnValue(Object returnValue);

    void onMethodException(Throwable exception);

    void onConstructorEnter(Class clazz, Object object, String constructorDescriptor);

    void onConstructorEnter(Class clazz, Object object, String constructorDescriptor, Object[] arguments);

    void onConstructorReturn();

    void onConstructorException(Class clazz, String constructorDescriptor, Throwable exception);

}
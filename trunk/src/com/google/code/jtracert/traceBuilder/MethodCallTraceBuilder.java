package com.google.code.jtracert.traceBuilder;

/**
 * @author Dmitry Bedrin
 */
public interface MethodCallTraceBuilder extends ConfigurableAnalyzer {

    void enter(String className, String methodName, String methodDescriptor, Object object, Object[] arguments/*, JTracertObjectCompanion jTracertObjectCompanion*/);

    void leave();
    void leave(Object returnValue);

    void exception(Throwable e);

    void leaveConstructor(String methodDescriptor);
//    void leaveConstructor(String className, String methodName, String methodDescriptor, Object object, Object[] arguments/*, JTracertObjectCompanion jTracertObjectCompanion*/);
    void leaveConstructor(String className, String methodName, String methodDescriptor, Throwable exception);

    Object wrap(Object o);
    void wrap();

}

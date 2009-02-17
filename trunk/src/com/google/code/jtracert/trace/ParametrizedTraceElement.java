package com.google.code.jtracert.trace;

public abstract class ParametrizedTraceElement extends TraceElement {

    private String descriptor;
    private Object[] arguments;

    protected ParametrizedTraceElement(Class clazz, Object object, String descriptor, Object[] arguments) {
        this(clazz, object, descriptor);
        this.arguments = arguments;
    }

    protected ParametrizedTraceElement(Class clazz, Object object, String descriptor) {
        super(clazz, object);
        this.descriptor = descriptor;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

}

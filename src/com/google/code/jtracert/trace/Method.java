package com.google.code.jtracert.trace;

public class Method extends ParametrizedTraceElement {

    private String name;

    public Method(Class clazz, Object object, String descriptor, Object[] arguments, String name) {
        super(clazz, object, descriptor, arguments);
        this.name = name;
    }

    public Method(Class clazz, Object object, String descriptor, String name) {
        super(clazz, object, descriptor);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

package com.google.code.jtracert.trace;

public class Constructor extends ParametrizedTraceElement {

    private static final String METHOD_NAME = "<init>";

    public Constructor(Class clazz, Object object, String descriptor, Object[] arguments) {
        super(clazz, object, descriptor, arguments);
    }

    public Constructor(Class clazz, Object object, String descriptor) {
        super(clazz, object, descriptor);
    }

    public String getName() {
        return METHOD_NAME;
    }

}

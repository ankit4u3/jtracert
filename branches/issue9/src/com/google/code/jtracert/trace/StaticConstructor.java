package com.google.code.jtracert.trace;

public class StaticConstructor extends TraceElement {

    private static final String METHOD_NAME = "<clinit>";

    public StaticConstructor(Class clazz) {
        super(clazz, clazz);
    }

    public String getName() {
        return METHOD_NAME;
    }

    @Override
    public Object getObject() {
        return super.getClazz();
    }

    @Override
    public void setObject(Object object) {

    }

}
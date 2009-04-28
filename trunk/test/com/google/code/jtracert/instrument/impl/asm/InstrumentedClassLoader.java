package com.google.code.jtracert.instrument.impl.asm;

public class InstrumentedClassLoader extends ClassLoader {

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {

        if (name.startsWith("com.google.code.jtracert") && !name.startsWith("com.google.code.jtracert.samples")) {
            return getSystemClassLoader().loadClass(name);
        }
        a();
        return super.loadClass(name);
    }

    private void a(){}

}

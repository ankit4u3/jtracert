package com.google.code.jtracert.instrument.impl.asm;

public class InstrumentedClassLoader extends ClassLoader {

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {

        if (
                name.startsWith("com.google.code.jtracert") &&
                !name.startsWith("com.google.code.jtracert.samples") &&
                this instanceof ClassLoader) {
            System.out.println(name);
            Thread.dumpStack();
            return ClassLoader.getSystemClassLoader().loadClass(name);
        }
        a();
        return super.loadClass(name);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {

        if (
                name.startsWith("com.google.code.jtracert") &&
                !name.startsWith("com.google.code.jtracert.samples") &&
                this instanceof ClassLoader) {
            System.out.println(name);
            Thread.dumpStack();
            return ClassLoader.getSystemClassLoader().loadClass(name);
        }
        a();

        return super.loadClass(name, resolve);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        if (
                name.startsWith("com.google.code.jtracert") &&
                !name.startsWith("com.google.code.jtracert.samples") &&
                this instanceof ClassLoader) {
            System.out.println(name);
            Thread.dumpStack();
            return ClassLoader.getSystemClassLoader().loadClass(name);
        }
        a();

        return super.findClass(name);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private void a(){}

}

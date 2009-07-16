package com.google.code.jtracert.instrument.impl.asm;

import java.lang.reflect.Method;

public class HandleObjectConstructor {

    /**
     * @todo add code below to classes loaded by bootstrap class loader 
     */
    public void method() throws Exception {
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();

        Class<?> mctbfClass = systemClassLoader.loadClass("com.google.code.jtracert.traceBuilder.MethodCallTraceBuilderFactory");

            ThreadLocal<Boolean> instrumenting = (ThreadLocal<Boolean>) mctbfClass.getField("instrumenting").get(null);

            if (!instrumenting.get()) {
                instrumenting.set(Boolean.TRUE);
                Method method = mctbfClass.getMethod("test");
                method.invoke(null);
                instrumenting.set(Boolean.FALSE);
            }

        System.out.println("Done!");
    }

}

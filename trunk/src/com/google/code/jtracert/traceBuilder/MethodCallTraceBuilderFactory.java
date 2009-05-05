package com.google.code.jtracert.traceBuilder;

import com.google.code.jtracert.config.AnalyzeProperties;
import com.google.code.jtracert.traceBuilder.impl.MethodCallTraceBuilderImpl;

import java.lang.reflect.Method;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class MethodCallTraceBuilderFactory {

    private static MethodCallTraceBuilder methodCallTraceBuilder;
    private static Method methodCallTraceBuilderFactoryMethod;

    private static final String FACTORY_METHOD_NAME = "getMethodCallTraceBuilder";

    /**
     * @return
     */
    public static MethodCallTraceBuilder getMethodCallTraceBuilder() {
        if (null == methodCallTraceBuilder) {
            methodCallTraceBuilder = new MethodCallTraceBuilderImpl();
        }
        return methodCallTraceBuilder;
    }

    /**
     * @param analyzeProperties
     */
    public static void configureMethodCallTraceBuilder(AnalyzeProperties analyzeProperties) {
        getMethodCallTraceBuilder().setAnalyzeProperties(analyzeProperties);
    }

    /**
     * @param methodCallTraceBuilder
     */
    public static void setMethodCallTraceBuilder(MethodCallTraceBuilder methodCallTraceBuilder) {
        MethodCallTraceBuilderFactory.methodCallTraceBuilder = methodCallTraceBuilder;
    }

    /**
     * @return
     */
    public static Method getMethodCallTraceBuilderFactoryMethod() {
        if (null == methodCallTraceBuilderFactoryMethod) {
            try {
                methodCallTraceBuilderFactoryMethod =
                        MethodCallTraceBuilderFactory.class.
                                getMethod(FACTORY_METHOD_NAME, (Class) null);
            } catch (NoSuchMethodException e) {
                methodCallTraceBuilderFactoryMethod = null;
            }
        }
        return methodCallTraceBuilderFactoryMethod;
    }

    public static Object[] objects = new Object[100];
    public static int i = 0;

    /**
     * This method is called from java.lang.Object constructor and hence cannot create new objects
     * creating arrays is permitted however
     *
     * @todo imlement double buffer and process created objects in a separate thread
     *
     * when jTracert is working, this method shouldn't be called for jTracert internal classes
     * @todo implement a thread local boolean flag using java.lang.Thread class instrumentation and
     * @todo handling an arrays of boolean flags in this method
     *
     * The class, this method belongs to, must be loaded by bootstrap constructor
     * @todo instrument class like java.lang.System and add this method to System class using instrumentation
     *
     * @param o
     */
    public synchronized static void constructor(Object o) {
        if (o instanceof String) {
            i++;
            objects[i % 100] = o;
        }
    }

}

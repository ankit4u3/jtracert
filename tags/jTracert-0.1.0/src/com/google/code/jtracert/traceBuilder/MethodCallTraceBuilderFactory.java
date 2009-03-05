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

}

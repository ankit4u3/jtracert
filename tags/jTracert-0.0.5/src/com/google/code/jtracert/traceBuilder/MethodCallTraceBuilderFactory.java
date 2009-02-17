package com.google.code.jtracert.traceBuilder;

import com.google.code.jtracert.config.AnalyzeProperties;
import com.google.code.jtracert.traceBuilder.impl.MethodCallTraceBuilderImpl;

import java.lang.reflect.Method;

/**
 * @author Dmitry Bedrin
 */
public class MethodCallTraceBuilderFactory {

    private static MethodCallTraceBuilder methodCallTraceBuilder;
    private static Method methodCallTraceBuilderFactoryMethod;

    private static final String FACTORY_METHOD_NAME = "getMethodCallTraceBuilder";

    public static MethodCallTraceBuilder getMethodCallTraceBuilder() {
        if (null == methodCallTraceBuilder) {
            methodCallTraceBuilder = new MethodCallTraceBuilderImpl();
        }
        return methodCallTraceBuilder;
    }

    public static void configureMethodCallTraceBuilder(AnalyzeProperties analyzeProperties) {
        getMethodCallTraceBuilder().setAnalyzeProperties(analyzeProperties);
    }

    public static void setMethodCallTraceBuilder(MethodCallTraceBuilder methodCallTraceBuilder) {
        MethodCallTraceBuilderFactory.methodCallTraceBuilder = methodCallTraceBuilder;
    }

    public static Method getMethodCallTraceBuilderFactoryMethod() {
        if (null == methodCallTraceBuilderFactoryMethod) {
            try {
                methodCallTraceBuilderFactoryMethod =
                        MethodCallTraceBuilderFactory.class.
                        getMethod(FACTORY_METHOD_NAME,(Class)null);
            } catch (NoSuchMethodException e) {
                methodCallTraceBuilderFactoryMethod = null;
            }
        }
        return methodCallTraceBuilderFactoryMethod;
    }

}

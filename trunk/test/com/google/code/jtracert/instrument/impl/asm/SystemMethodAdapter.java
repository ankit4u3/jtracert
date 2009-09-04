package com.google.code.jtracert.instrument.impl.asm;

import com.google.code.jtracert.traceBuilder.MethodCallTraceBuilderFactory;
import com.google.code.jtracert.traceBuilder.MethodCallTraceBuilder;

import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: dmitrybedrin
 * Date: Jun 16, 2009
 * Time: 12:19:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class SystemMethodAdapter {

    public void onMethodEnter() throws Exception {
        Class<MethodCallTraceBuilderFactory> methodCallTraceBuilderFactoryClass = MethodCallTraceBuilderFactory.class;

        Method getMethodCallTraceBuilder =
                methodCallTraceBuilderFactoryClass.getMethod("getMethodCallTraceBuilder",null);

        Object methodCallTraceBuilder = getMethodCallTraceBuilder.invoke(null,null);

        Class<?> methodCallTraceBuilderClass = ClassLoader.getSystemClassLoader().loadClass("com.google.code.jtracert.traceBuilder.MethodCallTraceBuilder");

        Method enterMethod = methodCallTraceBuilderClass.getMethod("enter",new Class[]{String.class, String.class, String.class, Object.class});

        enterMethod.invoke(methodCallTraceBuilder,"class","methodName","methodDesc", this);

    }

    public void onMethodExit() throws Exception {
        Class factoryClass =
                ClassLoader.getSystemClassLoader().loadClass("com.google.code.jtracert.traceBuilder.MethodCallTraceBuilderFactory");

        Method getMethodCallTraceBuilderMethod =
                factoryClass.getMethod("getMethodCallTraceBuilder",null);

        Object methodCallTraceBuilder = getMethodCallTraceBuilderMethod.invoke(null,null);

        Class<?> methodCallTraceBuilderClass = ClassLoader.getSystemClassLoader().loadClass("com.google.code.jtracert.traceBuilder.MethodCallTraceBuilder");

        Method enterMethod = methodCallTraceBuilderClass.getMethod("leave",null);

        enterMethod.invoke(methodCallTraceBuilder);

    }

}

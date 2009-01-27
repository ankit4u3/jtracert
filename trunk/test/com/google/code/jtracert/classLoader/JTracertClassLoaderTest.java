package com.google.code.jtracert.classLoader;

import com.google.code.jtracert.classFilter.impl.AllowClassByNameFilter;
import com.google.code.jtracert.config.AnalyzeProperties;
import com.google.code.jtracert.model.JTracertObjectCompanion;
import com.google.code.jtracert.traceBuilder.MethodCallTraceBuilder;
import com.google.code.jtracert.traceBuilder.MethodCallTraceBuilderFactory;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Dmitry Bedrin
 */
public class JTracertClassLoaderTest extends TestCase {

    private JTracertClassLoader jTracertClassLoader;

    @Before
    public void setUp() {
        ClassLoader currentClassLoader = JTracertClassLoaderTest.class.getClassLoader();

        jTracertClassLoader = new JTracertClassLoader(currentClassLoader);

        jTracertClassLoader.getClassFilterProcessor().addFilter(
                new AllowClassByNameFilter("com.google.code.jtracert.classLoader.ExperimentalClass1"));
    }

    @Test
    public void testClassLoaderProcessMethodBodyCorrectly() {

        try {

            Class experimentalClass1 = jTracertClassLoader.loadClass("com.google.code.jtracert.classLoader.ExperimentalClass1");

            Object experimentalObject1 = experimentalClass1.newInstance();

            Method experimentalMethod = experimentalClass1.getMethod("increment",int.class);

            for (int i = 0; i < 5; i ++) {

                int inputValue = i;

                Object resultValue = experimentalMethod.invoke(experimentalObject1, inputValue);

                int resultInt = (Integer) resultValue;

                assertEquals(resultInt, inputValue + 1);

            }

        } catch (ClassNotFoundException e) {
            fail();
        } catch (IllegalAccessException e) {
            fail();
        } catch (InstantiationException e) {
            fail();
        } catch (NoSuchMethodException e) {
            fail();
        } catch (InvocationTargetException e) {
            fail();
        }

    }

    @Test
    public void testCallTraceBuilt() {

        try {

            MethodCallTraceBuilderInvokeCounter methodCallTraceBuilderInvokeCounter =
                    new MethodCallTraceBuilderInvokeCounter();

            MethodCallTraceBuilderFactory.setMethodCallTraceBuilder(methodCallTraceBuilderInvokeCounter);

            Class experimentalClass1 = jTracertClassLoader.loadClass("com.google.code.jtracert.classLoader.ExperimentalClass1");

            Object experimentalObject1 = experimentalClass1.newInstance();

            assertNotNull(experimentalObject1);

            assertEquals(1,methodCallTraceBuilderInvokeCounter.getEnterMethodCount());
            assertEquals(1,methodCallTraceBuilderInvokeCounter.getLeaveOnReturnVoidCount());
            assertEquals(0,methodCallTraceBuilderInvokeCounter.getLeaveOnReturnValueCount());
            assertEquals(0,methodCallTraceBuilderInvokeCounter.getLeaveOnThrowExceptionValueCount());


        } catch (ClassNotFoundException e) {
            fail();
        } catch (IllegalAccessException e) {
            fail();
        } catch (InstantiationException e) {
            fail();
        }

    }

    @Test
    public void testCallTraceBuiltMethodReturnVoid() {

        try {

            MethodCallTraceBuilderInvokeCounter methodCallTraceBuilderInvokeCounter =
                    new MethodCallTraceBuilderInvokeCounter();

            MethodCallTraceBuilderFactory.setMethodCallTraceBuilder(methodCallTraceBuilderInvokeCounter);

            Class experimentalClass1 = jTracertClassLoader.loadClass("com.google.code.jtracert.classLoader.ExperimentalClass1");

            Object experimentalObject1 = experimentalClass1.newInstance();

            Method experimentalMethod = experimentalClass1.getMethod("methodReturnVoid");
            experimentalMethod.invoke(experimentalObject1);

            assertNotNull(experimentalObject1);

            assertEquals(2,methodCallTraceBuilderInvokeCounter.getEnterMethodCount());
            assertEquals(2,methodCallTraceBuilderInvokeCounter.getLeaveOnReturnVoidCount());
            assertEquals(0,methodCallTraceBuilderInvokeCounter.getLeaveOnReturnValueCount());
            assertEquals(0,methodCallTraceBuilderInvokeCounter.getLeaveOnThrowExceptionValueCount());


        } catch (ClassNotFoundException e) {
            fail();
        } catch (IllegalAccessException e) {
            fail();
        } catch (InstantiationException e) {
            fail();
        } catch (NoSuchMethodException e) {
            fail();
        } catch (InvocationTargetException e) {
            fail();
        }

    }

    @Test
    public void testCallTraceBuiltEmptyMethod() {

        try {

            MethodCallTraceBuilderInvokeCounter methodCallTraceBuilderInvokeCounter =
                    new MethodCallTraceBuilderInvokeCounter();

            MethodCallTraceBuilderFactory.setMethodCallTraceBuilder(methodCallTraceBuilderInvokeCounter);

            Class experimentalClass1 = jTracertClassLoader.loadClass("com.google.code.jtracert.classLoader.ExperimentalClass1");

            Object experimentalObject1 = experimentalClass1.newInstance();

            Method experimentalMethod = experimentalClass1.getMethod("emptyMethod");
            experimentalMethod.invoke(experimentalObject1);

            assertNotNull(experimentalObject1);

            assertEquals(2,methodCallTraceBuilderInvokeCounter.getEnterMethodCount());
            assertEquals(2,methodCallTraceBuilderInvokeCounter.getLeaveOnReturnVoidCount());
            assertEquals(0,methodCallTraceBuilderInvokeCounter.getLeaveOnReturnValueCount());
            assertEquals(0,methodCallTraceBuilderInvokeCounter.getLeaveOnThrowExceptionValueCount());


        } catch (ClassNotFoundException e) {
            fail();
        } catch (IllegalAccessException e) {
            fail();
        } catch (InstantiationException e) {
            fail();
        } catch (NoSuchMethodException e) {
            fail();
        } catch (InvocationTargetException e) {
            fail();
        }

    }

    @Test
    public void testCallTraceBuiltMethodReturnObject() {

        try {

            MethodCallTraceBuilderInvokeCounter methodCallTraceBuilderInvokeCounter =
                    new MethodCallTraceBuilderInvokeCounter();

            MethodCallTraceBuilderFactory.setMethodCallTraceBuilder(methodCallTraceBuilderInvokeCounter);

            Class experimentalClass1 = jTracertClassLoader.loadClass("com.google.code.jtracert.classLoader.ExperimentalClass1");

            Object experimentalObject1 = experimentalClass1.newInstance();

            Method experimentalMethod = experimentalClass1.getMethod("methodReturnObject");
            experimentalMethod.invoke(experimentalObject1);

            assertNotNull(experimentalObject1);

            assertEquals(2,methodCallTraceBuilderInvokeCounter.getEnterMethodCount());
            assertEquals(1,methodCallTraceBuilderInvokeCounter.getLeaveOnReturnVoidCount());
            assertEquals(1,methodCallTraceBuilderInvokeCounter.getLeaveOnReturnValueCount());
            assertEquals(0,methodCallTraceBuilderInvokeCounter.getLeaveOnThrowExceptionValueCount());


        } catch (ClassNotFoundException e) {
            fail();
        } catch (IllegalAccessException e) {
            fail();
        } catch (InstantiationException e) {
            fail();
        } catch (NoSuchMethodException e) {
            fail();
        } catch (InvocationTargetException e) {
            fail();
        }

    }

    @Test
    public void testCallTraceBuiltMethodThrowException() {

        try {

            MethodCallTraceBuilderInvokeCounter methodCallTraceBuilderInvokeCounter =
                    new MethodCallTraceBuilderInvokeCounter();

            MethodCallTraceBuilderFactory.setMethodCallTraceBuilder(methodCallTraceBuilderInvokeCounter);

            Class experimentalClass1 = jTracertClassLoader.loadClass("com.google.code.jtracert.classLoader.ExperimentalClass1");

            Object experimentalObject1 = experimentalClass1.newInstance();

            Method experimentalMethod = experimentalClass1.getMethod("methodThrowException");

            try {
                experimentalMethod.invoke(experimentalObject1);
                fail();
            } catch (InvocationTargetException e) {
                assertNotNull(e);
            }

            assertNotNull(experimentalObject1);

            assertEquals(2,methodCallTraceBuilderInvokeCounter.getEnterMethodCount());
            assertEquals(1,methodCallTraceBuilderInvokeCounter.getLeaveOnReturnVoidCount());
            assertEquals(0,methodCallTraceBuilderInvokeCounter.getLeaveOnReturnValueCount());
            assertEquals(1,methodCallTraceBuilderInvokeCounter.getLeaveOnThrowExceptionValueCount());


        } catch (ClassNotFoundException e) {
            fail();
        } catch (IllegalAccessException e) {
            fail();
        } catch (InstantiationException e) {
            fail();
        } catch (NoSuchMethodException e) {
            fail();
        }

    }

    @Test
    public void testCallTraceBuiltMethodReThrowException() {

        try {

            MethodCallTraceBuilderInvokeCounter methodCallTraceBuilderInvokeCounter =
                    new MethodCallTraceBuilderInvokeCounter();

            MethodCallTraceBuilderFactory.setMethodCallTraceBuilder(methodCallTraceBuilderInvokeCounter);

            Class experimentalClass1 = jTracertClassLoader.loadClass("com.google.code.jtracert.classLoader.ExperimentalClass1");

            Object experimentalObject1 = experimentalClass1.newInstance();

            Method experimentalMethod = experimentalClass1.getMethod("methodReThrowException");

            try {
                experimentalMethod.invoke(experimentalObject1);
                fail();
            } catch (InvocationTargetException e) {
                assertNotNull(e);
            }

            assertNotNull(experimentalObject1);

            assertEquals(3,methodCallTraceBuilderInvokeCounter.getEnterMethodCount());
            assertEquals(1,methodCallTraceBuilderInvokeCounter.getLeaveOnReturnVoidCount());
            assertEquals(0,methodCallTraceBuilderInvokeCounter.getLeaveOnReturnValueCount());
            assertEquals(2,methodCallTraceBuilderInvokeCounter.getLeaveOnThrowExceptionValueCount());


        } catch (ClassNotFoundException e) {
            fail();
        } catch (IllegalAccessException e) {
            fail();
        } catch (InstantiationException e) {
            fail();
        } catch (NoSuchMethodException e) {
            fail();
        }

    }

    private static class MethodCallTraceBuilderInvokeCounter
            implements MethodCallTraceBuilder {

        private int enterMethodCount = 0;
        private int leaveOnReturnVoidCount = 0;
        private int leaveOnReturnValueCount = 0;
        private int leaveOnThrowExceptionValueCount = 0;

        private AnalyzeProperties analyzeProperties;

        public AnalyzeProperties getAnalyzeProperties() {
            return analyzeProperties;
        }

        public void setAnalyzeProperties(AnalyzeProperties analyzeProperties) {
            this.analyzeProperties = analyzeProperties;
        }

        public void enter(String className, String methodName, String methodDescriptor, Object object, Object[] arguments/*, JTracertObjectCompanion jTracertObjectCompanion*/) {
            enterMethodCount = getEnterMethodCount() + 1;
        }

        public void leave() {
            leaveOnReturnVoidCount = getLeaveOnReturnVoidCount() + 1;
        }

        public void leave(Object returnValue) {
            leaveOnReturnValueCount = getLeaveOnReturnValueCount() + 1;
        }

        public void exception(Throwable e) {
            leaveOnThrowExceptionValueCount = getLeaveOnThrowExceptionValueCount() + 1;
        }

        public int getEnterMethodCount() {
            return enterMethodCount;
        }

        public int getLeaveOnReturnVoidCount() {
            return leaveOnReturnVoidCount;
        }

        public int getLeaveOnReturnValueCount() {
            return leaveOnReturnValueCount;
        }

        public int getLeaveOnThrowExceptionValueCount() {
            return leaveOnThrowExceptionValueCount;
        }

    }

}
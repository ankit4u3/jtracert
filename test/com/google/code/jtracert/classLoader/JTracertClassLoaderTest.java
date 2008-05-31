package com.google.code.jtracert.classLoader;

import junit.framework.TestCase;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import com.google.code.jtracert.filter.impl.InheritClassFilter;
import com.google.code.jtracert.filter.FilterAction;

/**
 * @author Dmitry Bedrin
 */
public class JTracertClassLoaderTest extends TestCase {

    @Test
    public void testClassLoaderSanity() {

        ClassLoader currentClassLoader = JTracertClassLoaderTest.class.getClassLoader();
        JTracertClassLoader jTracertClassLoader = new JTracertClassLoader(currentClassLoader);

        jTracertClassLoader.getClassFilterProcessor().addFilter(new InheritClassFilter() {

            public FilterAction filterClassName(String className) {
                if ("com.google.code.jtracert.classLoader.ExperimentalClass1".equals(className)) {
                    return FilterAction.ALLOW;
                } else {
                    return super.filterClassName(className);
                }
            }
        });

        try {

            Class experimentalClass1 =
                    jTracertClassLoader.loadClass("com.google.code.jtracert.classLoader.ExperimentalClass1");

            Object experimentalObject1 = experimentalClass1.newInstance();

            Method experimentalMethod = experimentalClass1.getMethod("experimentalMethod",int.class);

            Object resultValue = experimentalMethod.invoke(experimentalObject1,1);

            int resultInt = ((Integer)resultValue).intValue();

            assertEquals(resultInt, 2);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail();
        } catch (InstantiationException e) {
            e.printStackTrace();
            fail();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail();
        }

    }

}

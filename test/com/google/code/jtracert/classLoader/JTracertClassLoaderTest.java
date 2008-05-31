package com.google.code.jtracert.classLoader;

import com.google.code.jtracert.filter.impl.AllowClassByNameFilter;
import junit.framework.TestCase;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Dmitry Bedrin
 */
public class JTracertClassLoaderTest extends TestCase {

    @Test
    public void testClassLoaderSanity() {

        ClassLoader currentClassLoader = JTracertClassLoaderTest.class.getClassLoader();
        JTracertClassLoader jTracertClassLoader = new JTracertClassLoader(currentClassLoader);

        String experimentalClass1ClassName = "com.google.code.jtracert.classLoader.ExperimentalClass1";

        jTracertClassLoader.getClassFilterProcessor().addFilter(
                new AllowClassByNameFilter(experimentalClass1ClassName));

        try {

            Class experimentalClass1 = jTracertClassLoader.loadClass(experimentalClass1ClassName);

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

}

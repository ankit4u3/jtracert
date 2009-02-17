package com.google.code.jtracert.util;

/**
 * @author Dmitry Bedrin
 */
public class ClassUtils {

    public static String getFullyQualifiedName(String className) {
        return className.replace('/','.');
    }

}

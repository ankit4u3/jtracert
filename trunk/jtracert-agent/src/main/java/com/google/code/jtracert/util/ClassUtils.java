package com.google.code.jtracert.util;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class ClassUtils {

    public static final String CONSTRUCTOR_METHOD_NAME = "<init>";
    private static final String EMPTY_STRING = "";

    /**
     * @param className
     * @return
     */
    public static String getFullyQualifiedName(String className) {
        if (null == className) return EMPTY_STRING;
        return className.replace('/', '.');
    }

    /**
     * @param className
     * @return
     */
    public static String convertClassNameToResourceName(String className) {
        if (null == className) return EMPTY_STRING;
        return className.replace('.', '/') + ".class";
    }

}

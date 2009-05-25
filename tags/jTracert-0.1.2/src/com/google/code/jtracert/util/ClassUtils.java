package com.google.code.jtracert.util;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class ClassUtils {
    public static final String CONSTRUCTOR_METHOD_NAME = "<init>";

    /**
     * @param className
     * @return
     */
    public static String getFullyQualifiedName(String className) {
        return className.replace('/', '.');
    }

    /**
     * @param name
     * @return
     */
    public static String convertClassNameToResourceName(String name) {
        return name.replace('.', '/') + ".class";
    }

}

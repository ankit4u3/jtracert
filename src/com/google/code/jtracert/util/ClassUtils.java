package com.google.code.jtracert.util;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 * @author Dmitry.Bedrin@gmail.com
 */
public class ClassUtils {

    /**
     * 
     * @param className
     * @return
     */
    public static String getFullyQualifiedName(String className) {
        return className.replace('/','.');
    }

}

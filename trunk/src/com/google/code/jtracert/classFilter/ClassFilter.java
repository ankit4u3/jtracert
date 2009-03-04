package com.google.code.jtracert.classFilter;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public interface ClassFilter {

    /**
     * @param className
     * @return
     */
    FilterAction filterClassName(String className);

    /**
     * @param classLoader
     * @return
     */
    FilterAction filterClassLoader(ClassLoader classLoader);

}

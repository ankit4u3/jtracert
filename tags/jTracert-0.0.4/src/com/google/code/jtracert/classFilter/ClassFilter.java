package com.google.code.jtracert.classFilter;

/**
 * @author Dmitry Bedrin
 */
public interface ClassFilter {

    FilterAction filterClassName(String className);

    FilterAction filterClassLoader(ClassLoader classLoader);

}

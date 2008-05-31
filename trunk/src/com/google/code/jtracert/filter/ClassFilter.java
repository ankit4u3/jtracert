package com.google.code.jtracert.filter;

/**
 * @author Dmitry Bedrin
 */
public interface ClassFilter {

    FilterAction filterClassName(String className);

    FilterAction filterClassLoader(ClassLoader classLoader);

}

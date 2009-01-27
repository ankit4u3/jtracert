package com.google.code.jtracert.classFilter.impl;

import com.google.code.jtracert.classFilter.ClassFilter;
import com.google.code.jtracert.classFilter.FilterAction;

/**
 * @author Dmitry Bedrin
 */
public abstract class DefaultActionClassFilterAdapter implements ClassFilter {

    public FilterAction filterClassName(String className) {
        return getDefaultFilterAction();
    }

    public FilterAction filterClassLoader(ClassLoader classLoader) {
        return getDefaultFilterAction();
    }

    protected abstract FilterAction getDefaultFilterAction();

}

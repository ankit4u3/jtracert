package com.google.code.jtracert.filter.impl;

import com.google.code.jtracert.filter.ClassFilter;
import com.google.code.jtracert.filter.FilterAction;

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

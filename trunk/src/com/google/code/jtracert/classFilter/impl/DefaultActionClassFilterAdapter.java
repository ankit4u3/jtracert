package com.google.code.jtracert.classFilter.impl;

import com.google.code.jtracert.classFilter.ClassFilter;
import com.google.code.jtracert.classFilter.FilterAction;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 * @author Dmitry.Bedrin@gmail.com
 */
public abstract class DefaultActionClassFilterAdapter implements ClassFilter {

    /**
     *
     * @param className
     * @return
     */
    public FilterAction filterClassName(String className) {
        return getDefaultFilterAction();
    }

    /**
     * 
     * @param classLoader
     * @return
     */
    public FilterAction filterClassLoader(ClassLoader classLoader) {
        return getDefaultFilterAction();
    }

    protected abstract FilterAction getDefaultFilterAction();

}

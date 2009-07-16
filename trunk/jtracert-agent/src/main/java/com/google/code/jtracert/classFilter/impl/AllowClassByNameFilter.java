package com.google.code.jtracert.classFilter.impl;

import com.google.code.jtracert.classFilter.FilterAction;
import static com.google.code.jtracert.classFilter.FilterAction.ALLOW;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class AllowClassByNameFilter extends InheritClassFilter {

    private final String allowedClassName;

    /**
     * @param allowedClassName
     */
    public AllowClassByNameFilter(String allowedClassName) {
        this.allowedClassName = allowedClassName;
    }

    /**
     * @param className
     * @return
     */
    public FilterAction filterClassName(String className) {
        if (allowedClassName.equals(className)) {
            return ALLOW;
        } else {
            return super.filterClassName(className);
        }
    }
}

package com.google.code.jtracert.filter.impl;

import com.google.code.jtracert.filter.FilterAction;
import static com.google.code.jtracert.filter.FilterAction.ALLOW;

/**
 * @author dmitry.bedrin
 */
public class AllowClassByNameFilter extends InheritClassFilter {

    private final String allowedClassName;

    public AllowClassByNameFilter(String allowedClassName) {
        this.allowedClassName = allowedClassName;
    }

    public FilterAction filterClassName(String className) {
        if (allowedClassName.equals(className)) {
            return ALLOW;
        } else {
            return super.filterClassName(className);
        }
    }
}

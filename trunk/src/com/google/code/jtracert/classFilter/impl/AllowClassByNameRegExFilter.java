package com.google.code.jtracert.classFilter.impl;

import com.google.code.jtracert.classFilter.FilterAction;
import static com.google.code.jtracert.classFilter.FilterAction.DENY;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 * @author Dmitry.Bedrin@gmail.com
 */
public class AllowClassByNameRegExFilter extends InheritClassFilter {

    private final String allowedClassNameRegEx;

    /**
     *
     * @param allowedClassNameRegEx
     */
    public AllowClassByNameRegExFilter(String allowedClassNameRegEx) {
        this.allowedClassNameRegEx = allowedClassNameRegEx;
    }

    /**
     * 
     * @param className
     * @return
     */
    public FilterAction filterClassName(String className) {
        if (className.matches(allowedClassNameRegEx)) {
            return super.filterClassName(className);
        } else {
            return DENY;
        }
    }
}
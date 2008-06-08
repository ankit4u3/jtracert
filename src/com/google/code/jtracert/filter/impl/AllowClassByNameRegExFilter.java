package com.google.code.jtracert.filter.impl;

import com.google.code.jtracert.filter.FilterAction;
import static com.google.code.jtracert.filter.FilterAction.DENY;

/**
 * @author dmitry.bedrin
 */
public class AllowClassByNameRegExFilter extends InheritClassFilter {

    private final String allowedClassNameRegEx;

    public AllowClassByNameRegExFilter(String allowedClassNameRegEx) {
        this.allowedClassNameRegEx = allowedClassNameRegEx;
    }

    public FilterAction filterClassName(String className) {
        if (className.matches(allowedClassNameRegEx)) {
            return super.filterClassName(className);
        } else {
            return DENY;
        }
    }
}
package com.google.code.jtracert.classFilter.impl;

import com.google.code.jtracert.classFilter.FilterAction;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class DenyClassByPackageNameFilter extends InheritClassFilter {

    private final String deniedPackageName;

    /**
     * @param deniedPackageName
     */
    public DenyClassByPackageNameFilter(String deniedPackageName) {
        this.deniedPackageName = deniedPackageName;
    }

    /**
     * @param className
     * @return
     */
    public FilterAction filterClassName(String className) {
        if (className.startsWith(deniedPackageName)) {
            return FilterAction.DENY;
        } else {
            return super.filterClassName(className);
        }
    }
}

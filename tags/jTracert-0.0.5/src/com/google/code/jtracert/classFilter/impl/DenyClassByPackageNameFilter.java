package com.google.code.jtracert.classFilter.impl;

import com.google.code.jtracert.classFilter.FilterAction;

/**
 * @author dmitry.bedrin
 */
public class DenyClassByPackageNameFilter extends InheritClassFilter {

    private final String deniedPackageName;

    public DenyClassByPackageNameFilter(String deniedPackageName) {
        this.deniedPackageName = deniedPackageName;
    }

    public FilterAction filterClassName(String className) {
        if (className.startsWith(deniedPackageName)) {
            return FilterAction.DENY;
        } else {
            return super.filterClassName(className);
        }
    }
}

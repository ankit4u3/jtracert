package com.google.code.jtracert.filter.impl;

import com.google.code.jtracert.ProjectInfo;
import com.google.code.jtracert.filter.FilterAction;
import static com.google.code.jtracert.filter.FilterAction.DENY;

/**
 * @author dmitry.bedrin
 */
public class DenyJTracertClassesFilter extends InheritClassFilter {

    @Override
    public FilterAction filterClassName(String className) {
        if (className.startsWith(ProjectInfo.PROJECT_PACKAGE_NAME)) {
            return DENY;
        } else {
            return super.filterClassName(className);
        }
    }
    
}

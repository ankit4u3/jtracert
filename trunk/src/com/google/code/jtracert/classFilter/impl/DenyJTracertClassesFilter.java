package com.google.code.jtracert.classFilter.impl;

import com.google.code.jtracert.ProjectInfo;
import com.google.code.jtracert.classFilter.FilterAction;
import static com.google.code.jtracert.classFilter.FilterAction.DENY;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 * @author Dmitry.Bedrin@gmail.com
 */
public class DenyJTracertClassesFilter extends InheritClassFilter {

    /**
     * 
     * @param className
     * @return
     */
    @Override
    public FilterAction filterClassName(String className) {
        if (className.startsWith(ProjectInfo.PROJECT_PACKAGE_NAME)) {
            if (className.startsWith(ProjectInfo.PROJECT_PACKAGE_NAME + ".samples")) {
                return super.filterClassName(className);
            } else {
                return DENY;
            }
        } else {
            return super.filterClassName(className);
        }
    }
    
}

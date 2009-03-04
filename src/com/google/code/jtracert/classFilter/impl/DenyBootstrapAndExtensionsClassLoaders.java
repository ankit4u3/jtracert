package com.google.code.jtracert.classFilter.impl;

import com.google.code.jtracert.classFilter.FilterAction;
import static com.google.code.jtracert.classFilter.FilterAction.DENY;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 * @author Dmitry.Bedrin@gmail.com
 */
public class DenyBootstrapAndExtensionsClassLoaders extends InheritClassFilter {

    /**
     * 
     * @param classLoader
     * @return
     */
    @Override
    public FilterAction filterClassLoader(ClassLoader classLoader) {

        if (null == classLoader) {
            // Bootstrap class loader
            return DENY;
        }

        ClassLoader currentClassLoader = ClassLoader.getSystemClassLoader().getParent();

        boolean isBootstrapOrExtensionsClassLoader = false;

        do {
            if (currentClassLoader == classLoader) {
                isBootstrapOrExtensionsClassLoader = true;
                break;
            } else {
                currentClassLoader = currentClassLoader.getParent();
            }
        } while (null != currentClassLoader);

        if (isBootstrapOrExtensionsClassLoader) {
            return DENY;
        } else {
            return super.filterClassLoader(classLoader);
        }

    }
}

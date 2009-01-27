package com.google.code.jtracert.classFilter.impl;

import com.google.code.jtracert.classFilter.FilterAction;
import static com.google.code.jtracert.classFilter.FilterAction.DENY;

/**
 * @author Dmitry Bedrin
 */
public class DenyBootstrapAndExtensionsClassLoaders extends InheritClassFilter {

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

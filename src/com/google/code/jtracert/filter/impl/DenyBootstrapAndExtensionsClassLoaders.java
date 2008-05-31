package com.google.code.jtracert.filter.impl;

import com.google.code.jtracert.filter.FilterAction;
import static com.google.code.jtracert.filter.FilterAction.ALLOW;

/**
 * @author Dmitry Bedrin
 */
public class DenyBootstrapAndExtensionsClassLoaders extends InheritClassFilter {

    @Override
    public FilterAction filterClassLoader(ClassLoader classLoader) {

        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        ClassLoader currentClassLoader = classLoader;

        boolean systemClassLoaderOrAncestor = false;

        do {
            if (currentClassLoader == systemClassLoader) {
                systemClassLoaderOrAncestor = true;
                break;
            } else {
                currentClassLoader = currentClassLoader.getParent();
            }
        } while (null != currentClassLoader);

        if (systemClassLoaderOrAncestor) {
            return ALLOW;
        } else {
            return super.filterClassLoader(classLoader);
        }

    }
}

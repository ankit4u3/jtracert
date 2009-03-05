package com.google.code.jtracert.classFilter;

import static com.google.code.jtracert.classFilter.FilterAction.ALLOW;
import static com.google.code.jtracert.classFilter.FilterAction.DENY;
import com.google.code.jtracert.classFilter.impl.*;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * @author Dmitry Bedrin
 */
public class ClassFilterProcessor {

    private Collection<ClassFilter> classFilters = new LinkedList<ClassFilter>();

    public ClassFilterProcessor() {
        addFilter(new AllowClassFilter());
        addFilter(new DenyJTracertClassesFilter());
        addFilter(new DenyBootstrapAndExtensionsClassLoaders());
        addFilter(new DenyClassByPackageNameFilter("sun.reflect"));

        // temporary workaround for issue 3
//        addFilter(new DenyClassByPackageNameFilter("com.sun.crypto.provider.SunJCE")); // todo - fix issue
    }

    public ClassFilterProcessor(Collection<ClassFilter> classFilters) {
        this.classFilters = classFilters;
    }

    public synchronized boolean addFilter(ClassFilter classFilter) {
        return classFilters.add(classFilter);
    }

    public synchronized boolean removeClassFilter(ClassFilter classFilter) {
        return classFilters.remove(classFilter);
    }

    public Collection<ClassFilter> getClassFilters() {
        return Collections.unmodifiableCollection(classFilters);
    }

    public synchronized boolean processClass(String className, ClassLoader classLoader) {

        FilterAction classNameFilterAction = ALLOW;
        FilterAction classLoaderFilterAction = ALLOW;

        for (ClassFilter classFilter : classFilters) {
            classNameFilterAction =
                    classNameFilterAction.apply(classFilter.filterClassName(className));
            classLoaderFilterAction =
                    classLoaderFilterAction.apply(classFilter.filterClassLoader(classLoader));
        }

        if ((DENY == classNameFilterAction) || (DENY == classLoaderFilterAction)) {
            return false;
        } else {
            return true;
        }

    }

}
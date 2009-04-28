package com.google.code.jtracert.samples;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.eclipse.core.runtime.adaptor.EclipseStarter;

public class Equinox implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        System.out.println("start");
        method1();
        System.exit(1);
    }


    public void stop(BundleContext context) throws Exception {
        System.out.println("stop");
        method2();
    }

    private static void method1() {
        method2();
        method2();
    }

    private static void method2() {
    }

}
package com.google.code.jtracert.samples;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Bundle;
import org.eclipse.core.runtime.adaptor.EclipseStarter;

public class Equinox implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        System.out.println("start");
        method1();
        startShutDownTimer(context);
    }

    private void startShutDownTimer(final BundleContext context) {
        new Thread( new Runnable() {

            public void run() {
                try {
                    Thread.sleep(1000);

                    for (Bundle bundle : context.getBundles()) {
                        if (bundle.getBundleId() == 0L) {
                            try {
                                bundle.stop();

                                while (Bundle.RESOLVED != bundle.getState()) {
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        System.exit(1);
                                    }
                                }
                            } catch (BundleException e) {
                                System.exit(1);
                            }
                            break ;
                        }
                    }
                } catch (Exception e) {
                    System.exit(1);
                }
            }

        }).start();
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
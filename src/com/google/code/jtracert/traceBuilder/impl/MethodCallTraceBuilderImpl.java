package com.google.code.jtracert.traceBuilder.impl;

import com.google.code.jtracert.model.JTracertObjectCompanion;
import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.traceBuilder.MethodCallTraceBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Dmitry Bedrin
 */
public class MethodCallTraceBuilderImpl implements MethodCallTraceBuilder {

    private ThreadLocal<Boolean> buildingMethodCallTrace = new ThreadLocal<Boolean>() {

        public Boolean initialValue() {
            return Boolean.FALSE;
        }

    };

    private ThreadLocal<MethodCall> contextMethodCall = new ThreadLocal<MethodCall>();

    private ExecutorService executorService =
            new ThreadPoolExecutor(0, 8, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    public void enter(String className, String methodName, String methodDescriptor, Object object, Object[] arguments, JTracertObjectCompanion jTracertObjectCompanion) {

        if (buildingMethodCallTrace.get()) return;

        try {

            buildingMethodCallTrace.set(Boolean.TRUE);

            // Bla

            MethodCall currentMethodCall = new MethodCall();

            MethodCall contextMethodCall = this.contextMethodCall.get();

            if (null != contextMethodCall) {
                contextMethodCall.addCallee(currentMethodCall);
            }

            this.contextMethodCall.set(currentMethodCall);

            // Bla

            currentMethodCall.setMethodName(methodName);
            currentMethodCall.setMethodSignature(methodDescriptor);

            currentMethodCall.setjTracertObjectCompanion(jTracertObjectCompanion);

            if (object == null) {
                currentMethodCall.setRealClassName(className);
            } else {
                currentMethodCall.setRealClassName(object.getClass().getName());
            }


        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            buildingMethodCallTrace.set(Boolean.FALSE);
        }

    }

    private void graphFinished(final MethodCall methodCall) {
        executorService.execute(new SDEditClientRunnable(methodCall));
    }

    private class SDEditClientRunnable implements Runnable {

        private MethodCall methodCall;

        public SDEditClientRunnable(MethodCall methodCall) {
            this.methodCall = methodCall;
        }

        public void run() {
            new SDEditClient().processMethodCall(methodCall);
        }

    }

    public void leave() {

        if (buildingMethodCallTrace.get()) return;

        try {

            buildingMethodCallTrace.set(Boolean.TRUE);

            MethodCall contextMethodCall = this.contextMethodCall.get();

            MethodCall callerMethodCall = contextMethodCall.getCalleer();
            if (null == callerMethodCall) {
                this.contextMethodCall.remove();
                graphFinished(contextMethodCall);
            } else {
                this.contextMethodCall.set(callerMethodCall);
            }

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            buildingMethodCallTrace.set(Boolean.FALSE);
        }

    }

    public void leave(Object returnValue) {
        leave();
    }

    public void exception(Throwable e) {
        leave();
    }

}
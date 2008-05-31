package com.google.code.jtracert.traceBuilder.impl;

import com.google.code.jtracert.model.JTracertObjectCompanion;
import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.traceBuilder.MethodCallTraceBuilder;

import java.util.concurrent.*;
import java.util.Set;
import java.util.HashSet;

/**
 * @author Dmitry Bedrin
 */
class MethodCallTraceBuilderState {

    public MethodCall methodCall = null;
    public boolean buildingTrace = false;
    public int graphHashCode = 17;
    public int level = 1;

}

class MethodCallTraceBuilderStateThreadLocal extends ThreadLocal<MethodCallTraceBuilderState> {

    protected MethodCallTraceBuilderState initialValue() {
        return new MethodCallTraceBuilderState();
    }
}

public class MethodCallTraceBuilderImpl implements MethodCallTraceBuilder {

    private static MethodCallTraceBuilderStateThreadLocal traceBuilderState = new MethodCallTraceBuilderStateThreadLocal();

    /*private ThreadLocal<Boolean> buildingMethodCallTrace = new ThreadLocal<Boolean>() {

        public Boolean initialValue() {
            return Boolean.FALSE;
        }

    };

    private ThreadLocal<MethodCall> contextMethodCall = new ThreadLocal<MethodCall>();*/

    private ExecutorService executorService =
            new ThreadPoolExecutor(0, 1, 1L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(5), new ThreadPoolExecutor.DiscardPolicy());


    private Set<Integer> processedHashCodes = new HashSet<Integer>();

    public void enter(String className, String methodName, String methodDescriptor, Object object, Object[] arguments, JTracertObjectCompanion jTracertObjectCompanion) {

        MethodCallTraceBuilderState state = traceBuilderState.get();

        if (state.buildingTrace) return;

        try {

            state.buildingTrace = true;

            // Bla

            MethodCall currentMethodCall = new MethodCall();

            MethodCall contextMethodCall = state.methodCall;

            if (null != contextMethodCall) {
                contextMethodCall.addCallee(currentMethodCall);
            }

            state.methodCall = currentMethodCall;
            state.level++;

            // Bla

            currentMethodCall.setMethodName(methodName);
            currentMethodCall.setMethodSignature(methodDescriptor);

            currentMethodCall.setjTracertObjectCompanion(jTracertObjectCompanion);

            int hashCode = state.graphHashCode;
            hashCode = (37 * (37 * hashCode + methodName.hashCode()) + state.level);

            /*if (null != jTracertObjectCompanion)
                hashCode = 37 * hashCode + jTracertObjectCompanion.hashCode();*/

            if (object == null) {
                currentMethodCall.setRealClassName(className);
            } else {
                currentMethodCall.setRealClassName(object.getClass().getName());
            }

            state.graphHashCode = hashCode;


        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            state.buildingTrace = false;
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

        MethodCallTraceBuilderState state = traceBuilderState.get();

        if (state.buildingTrace) return;

        try {

            state.buildingTrace = true;

            MethodCall contextMethodCall = state.methodCall;

            MethodCall callerMethodCall = contextMethodCall.getCalleer();
            if (null == callerMethodCall) {
                if (!processedHashCodes.contains(state.graphHashCode)) {
                    graphFinished(contextMethodCall);
                    processedHashCodes.add(state.graphHashCode);
                }
                traceBuilderState.remove();
            } else {
                state.methodCall = callerMethodCall;
                state.level--;
            }

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            state.buildingTrace = false;
        }

    }

    public void leave(Object returnValue) {
        leave();
    }

    public void exception(Throwable e) {
        leave();
    }

}

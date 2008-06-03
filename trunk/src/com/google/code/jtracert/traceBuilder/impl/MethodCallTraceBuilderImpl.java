package com.google.code.jtracert.traceBuilder.impl;

import com.google.code.jtracert.model.JTracertObjectCompanion;
import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.traceBuilder.MethodCallTraceBuilder;
import com.google.code.jtracert.traceBuilder.impl.graph.NormalizeMetodCallGraphVisitor;

import java.util.concurrent.*;
import java.util.Set;
import java.util.HashSet;
import java.io.OutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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

    @Override
    protected MethodCallTraceBuilderState initialValue() {
        return new MethodCallTraceBuilderState();
    }

}

class SizeOutputStream extends OutputStream {

    private long size = 0;

    @Override
    public void write(int b) throws IOException {
        size++;
    }

    @Override
    public void write(byte b[]) throws IOException {
        size += b.length;
    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        size += len;
    }

    public long getSize() {
        return size;
    }

}

public class MethodCallTraceBuilderImpl implements MethodCallTraceBuilder {

    private final static String newline = System.getProperty("line.separator");

    private static MethodCallTraceBuilderStateThreadLocal traceBuilderState = new MethodCallTraceBuilderStateThreadLocal();

    private ThreadPoolExecutor executorService;
    private Set<Integer> processedHashCodes;
    private boolean verbose = true;
    
    public MethodCallTraceBuilderImpl() {

        verbose = true;

        processedHashCodes = new HashSet<Integer>();

        executorService = new ThreadPoolExecutor(
                0,
                10,
                60L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(20,true),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

    }


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

        if (verbose) {

            try {
                SizeOutputStream out = new SizeOutputStream();
                ObjectOutputStream outputStream = new ObjectOutputStream(out);
                outputStream.writeObject(methodCall);
                outputStream.flush();
                long size = out.getSize();

                System.out.println();
                System.out.println("Call trace size is " + size + " bytes");
                System.out.println();

            } catch (IOException e) {
                e.printStackTrace();
            }

            StringBuffer executorServiceDebugInfo = new StringBuffer();
            executorServiceDebugInfo.append("ActiveCount").append(executorService.getActiveCount()).append(newline);
            executorServiceDebugInfo.append("CompletedTaskCount").append(executorService.getCompletedTaskCount()).append(newline);
            executorServiceDebugInfo.append("CorePoolSize").append(executorService.getCorePoolSize()).append(newline);
            executorServiceDebugInfo.append("LargestPoolSize").append(executorService.getLargestPoolSize()).append(newline);
            executorServiceDebugInfo.append("MaximumPoolSize").append(executorService.getMaximumPoolSize()).append(newline);
            executorServiceDebugInfo.append("PoolSize").append(executorService.getPoolSize()).append(newline);
            executorServiceDebugInfo.append("TaskCount").append(executorService.getTaskCount()).append(newline);

            System.out.println(executorServiceDebugInfo);

        }

        new SDEditClient().processMethodCall(methodCall);

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
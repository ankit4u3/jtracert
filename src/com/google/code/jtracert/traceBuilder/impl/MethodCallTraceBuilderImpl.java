package com.google.code.jtracert.traceBuilder.impl;

import com.google.code.jtracert.traceBuilder.impl.webSequenceDiagrams.WebSequenceDiagramsOutClient;
import com.google.code.jtracert.traceBuilder.impl.webSequenceDiagrams.WebSequenceDiagramsFileClient;
import com.google.code.jtracert.traceBuilder.impl.sequence.SequenceOutClient;
import com.google.code.jtracert.traceBuilder.impl.sequence.SequenceFileClient;
import com.google.code.jtracert.model.JTracertObjectCompanion;
import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.traceBuilder.MethodCallTraceBuilder;
import com.google.code.jtracert.traceBuilder.impl.graph.NormalizeMetodCallGraphVisitor;
import com.google.code.jtracert.traceBuilder.impl.graph.HashCodeBuilderMethodCallGraphVisitor;
import com.google.code.jtracert.traceBuilder.impl.sdedit.*;
import com.google.code.jtracert.traceBuilder.impl.serializableTcpClient.SerializableTcpClient;
import com.google.code.jtracert.util.SizeOutputStream;
import com.google.code.jtracert.util.FileUtils;
import com.google.code.jtracert.config.AnalyzeProperties;

import java.util.concurrent.*;
import java.util.Set;
import java.util.HashSet;
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
    public int count = 1;

}

class MethodCallTraceBuilderStateThreadLocal extends ThreadLocal<MethodCallTraceBuilderState> {

    @Override
    protected MethodCallTraceBuilderState initialValue() {
        return new MethodCallTraceBuilderState();
    }

}

public class MethodCallTraceBuilderImpl implements MethodCallTraceBuilder {

    private static MethodCallTraceBuilderStateThreadLocal traceBuilderState = new MethodCallTraceBuilderStateThreadLocal();

    private ThreadPoolExecutor executorService;
    private Set<Integer> processedHashCodes;

    public MethodCallTraceBuilderImpl() {

        processedHashCodes = new HashSet<Integer>();

        executorService = new ThreadPoolExecutor(
                0,
                1,
                5L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(20,true),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

    }


    public void enter(String className, String methodName, String methodDescriptor, Object object, Object[] arguments, JTracertObjectCompanion jTracertObjectCompanion) {

        jTracertObjectCompanion = null;

        MethodCallTraceBuilderState state = traceBuilderState.get();

        if (state.buildingTrace) return;

        try {

            state.buildingTrace = true;
            state.level++;
            state.count++;

            if (state.count > 1000) return;

            MethodCall currentMethodCall = new MethodCall();

            MethodCall contextMethodCall = state.methodCall;

            if (null == contextMethodCall) {
                // first enter
            } else {
                contextMethodCall.addCallee(currentMethodCall);
            }

            state.methodCall = currentMethodCall;

            currentMethodCall.setMethodName(methodName);
            currentMethodCall.setMethodSignature(methodDescriptor);

            currentMethodCall.setjTracertObjectCompanion(jTracertObjectCompanion);

            int hashCode = state.graphHashCode;
            hashCode = (37 * (37 * hashCode + methodName.hashCode()) + state.level); // todo refactor

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

        if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {

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
            executorServiceDebugInfo.append("ActiveCount").append(executorService.getActiveCount()).append(FileUtils.LINE_SEPARATOR);
            executorServiceDebugInfo.append("CompletedTaskCount").append(executorService.getCompletedTaskCount()).append(FileUtils.LINE_SEPARATOR);
            executorServiceDebugInfo.append("CorePoolSize").append(executorService.getCorePoolSize()).append(FileUtils.LINE_SEPARATOR);
            executorServiceDebugInfo.append("LargestPoolSize").append(executorService.getLargestPoolSize()).append(FileUtils.LINE_SEPARATOR);
            executorServiceDebugInfo.append("MaximumPoolSize").append(executorService.getMaximumPoolSize()).append(FileUtils.LINE_SEPARATOR);
            executorServiceDebugInfo.append("PoolSize").append(executorService.getPoolSize()).append(FileUtils.LINE_SEPARATOR);
            executorServiceDebugInfo.append("TaskCount").append(executorService.getTaskCount()).append(FileUtils.LINE_SEPARATOR);

            System.out.println(executorServiceDebugInfo);

        }

        executorService.execute(new SDEditClientRunnable(methodCall, getAnalyzeProperties()));

    }

    private AnalyzeProperties analyzeProperties;

    public AnalyzeProperties getAnalyzeProperties() {
        return analyzeProperties;
    }

    public void setAnalyzeProperties(AnalyzeProperties analyzeProperties) {
        this.analyzeProperties = analyzeProperties;
    }

    private class SDEditClientRunnable implements Runnable {

        private MethodCall methodCall;
        private AnalyzeProperties analyzeProperties;

        public SDEditClientRunnable(MethodCall methodCall, AnalyzeProperties analyzeProperties) {
            this.methodCall = methodCall;
            this.analyzeProperties = analyzeProperties;
        }

        public void run() {

            long currentTime = System.nanoTime();

            if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
                System.out.println("Normalizing Call Graph <<<");
            }
            methodCall.accept(new NormalizeMetodCallGraphVisitor());
            if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
                System.out.println("Normalizing Call Graph >>>");
                System.out.println("Took " + (System.nanoTime() - currentTime) + " nano seconds");
            }

            if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
                System.out.println("Calculating Call Graph Hash <<<");
            }
            int hashCode = methodCall.accept(new HashCodeBuilderMethodCallGraphVisitor());
            if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
                System.out.println("Calculating Call Graph Hash >>>");
                System.out.println("Took " + (System.nanoTime() - currentTime) + " nano seconds");
            }

            if (!processedHashCodes.contains(hashCode)) {
                processedHashCodes.add(hashCode);

                MethodCallProcessor methodCallProcessor = null;

                if (null != analyzeProperties) {
                    switch (analyzeProperties.getAnalyzerOutput()) {
                        case none:
                            break;
                        case sdEditOut:
                            methodCallProcessor = new SDEditOutClient();
                            break;
                        case sdEditRtClient:
                            methodCallProcessor = new SDEditRtClient();
                            break;
                        case sdEditFileSystem:
                            methodCallProcessor = new SDEditFileClient();
                            break;
                        case sequenceOut:
                            methodCallProcessor = new SequenceOutClient();
                            break;
                        case sequenceFileSystem:
                            methodCallProcessor = new SequenceFileClient();
                            break;
                        case webSequenceDiagramsOut:
                            methodCallProcessor = new WebSequenceDiagramsOutClient();
                            break;
                        case webSequenceDiagramsFileSystem:
                            methodCallProcessor = new WebSequenceDiagramsFileClient();
                            break;
                        case serializableTcpClient:
                            methodCallProcessor = new SerializableTcpClient();
                            break;
                    }
                }

                if (null != methodCallProcessor) {
                    methodCallProcessor.setAnalyzeProperties(analyzeProperties);

                    if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
                        System.out.println("Executing process method call for " + methodCall.getRealClassName() + "." + methodCall.getMethodName() + " <<<");
                    }
                    methodCallProcessor.processMethodCall(methodCall);
                    if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
                        System.out.println("Executing process method call for " + methodCall.getRealClassName() + "." + methodCall.getMethodName() + " >>>");
                    }

                }

            }

        }

    }

    public void leave() {

        MethodCallTraceBuilderState state = traceBuilderState.get();

        if (null == state) return;

        if (state.buildingTrace) return;

        try {

            state.buildingTrace = true;
            state.level--;

            MethodCall contextMethodCall = state.methodCall;

//            MethodCall callerMethodCall = contextMethodCall.getCalleer();
//            if (null == callerMethodCall) {
            if (1 == state.level) {

                if (state.count > 1000) {

                } else {
                    if (!processedHashCodes.contains(state.graphHashCode)) {
                        graphFinished(contextMethodCall);
                        processedHashCodes.add(state.graphHashCode);
                    }
                }
                //graphFinished(contextMethodCall);
                traceBuilderState.remove();
            } else {

                if (state.count > 1000) return;

                MethodCall callerMethodCall = contextMethodCall.getCalleer();
                state.methodCall = callerMethodCall;
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
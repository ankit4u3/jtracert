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
import java.util.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;

/**
 * @author Dmitry Bedrin
 */
class MethodCallTraceBuilderState {

    public MethodCall methodCall = null;
    public boolean buildingTrace = false;
//    public int graphHashCode = 17;
    public int level = 1;
    public int count = 1;

    public Map<String,Integer> enterConstructorLevel = new HashMap<String,Integer>();
    public Map<String,Integer> leaveConstructorLevel = new HashMap<String,Integer>();

    @Override
    public String toString() {
        return "MethodCallTraceBuilderState{" +
                "methodCall=" + methodCall +
                ", buildingTrace=" + buildingTrace +
//                ", graphHashCode=" + graphHashCode +
                ", level=" + level +
                ", count=" + count +
                '}';
    }
    
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
                new ArrayBlockingQueue<Runnable>(5,true),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

    }

    private Set<String> jarURLs = new HashSet<String>();

    public void enter(String className, String methodName, String methodDescriptor, Object object, Object[] arguments/*, JTracertObjectCompanion jTracertObjectCompanion*/) {

        //watchJar(object);
        MethodCallTraceBuilderState state = traceBuilderState.get();

        if (state.buildingTrace) return;

        try {

            state.buildingTrace = true;
            state.level++;
            state.count++;

            if (methodName.equals("<init>")) {

                Integer constructorLevelFromState;
                int constructorLevel;

                constructorLevelFromState = state.enterConstructorLevel.get(className);
                if (null == constructorLevelFromState) {
                    constructorLevel = 0;
                } else {
                    constructorLevel = constructorLevelFromState;
                }

                constructorLevel++;

                state.enterConstructorLevel.put(className, constructorLevel);

            }

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

            //currentMethodCall.setjTracertObjectCompanion(jTracertObjectCompanion);

            if (object == null) {
                currentMethodCall.setObjectHashCode(className.hashCode()); // Use Class object instead of String
            } else {
                currentMethodCall.setObjectHashCode(System.identityHashCode(object));
            }


            //int hashCode = state.graphHashCode;
            //hashCode = (37 * (37 * hashCode + methodName.hashCode()) + state.level); // todo refactor

            if (object == null) {
                currentMethodCall.setRealClassName(className);
            } else {
                currentMethodCall.setRealClassName(object.getClass().getName());
            }

            //state.graphHashCode = hashCode;

            System.out.println(className + "." + methodName + methodDescriptor +  " <<<");

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            state.buildingTrace = false;
        }

    }

    private void watchJar(Object object) {
        if (null != object) {
            URL codeLocationURL = object.getClass().getProtectionDomain().getCodeSource().getLocation();
            if (null != codeLocationURL) {
                String jarURL = codeLocationURL.toString();
                if (!jarURLs.contains(jarURL)) {
                    System.out.println("Dependency from JAR: " + jarURL);
                    jarURLs.add(jarURL);
                }
            }
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

    public void leave() {

        MethodCallTraceBuilderState state = traceBuilderState.get();

        if (null == state) return;

        if (state.buildingTrace) return;

        try {

            state.buildingTrace = true;
            state.level--;

            MethodCall contextMethodCall = state.methodCall;

            if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
                System.out.println(contextMethodCall.getRealClassName() + "." + contextMethodCall.getMethodName() + contextMethodCall.getMethodSignature() + " >>>");
            }

            if (1 == state.level) {

                if (state.count > 1000) {
                    if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
                        System.out.println("Too large trace detected - will not be processed");
                    }
                } else {
                    graphFinished(contextMethodCall);
                }
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

    public void leaveConstructor(String methodDescriptor) {

        if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
            System.out.println("Leaving constructor with desriptor " + methodDescriptor);
        }

        swapConstructors();

        leave();
    }

    private void swapConstructors() {
        MethodCallTraceBuilderState state = traceBuilderState.get();

        MethodCall currentMethodCall = state.methodCall;

        List<MethodCall> siblingCallees = currentMethodCall.getCalleer().getCallees();

        if (siblingCallees.size() > 1) {
            MethodCall previousSiblingMethodCall = siblingCallees.get(siblingCallees.size() - 2);

            if ((previousSiblingMethodCall.getMethodName().equals("<init>")) &&
                (previousSiblingMethodCall.getObjectHashCode() == currentMethodCall.getObjectHashCode())) {
                currentMethodCall.addCallee(previousSiblingMethodCall);
                siblingCallees.remove(previousSiblingMethodCall);

                if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
                    System.out.println("Swapping constructors!!!!");
                }

            }
        }
    }

    public void leaveConstructor(String className, String methodName, String methodDescriptor, Throwable exception) {

        System.out.println();
        System.out.println(System.identityHashCode(exception));
        System.out.println();

//        System.out.println("Leaving constructor on exception with desriptor " + methodDescriptor);

        MethodCallTraceBuilderState state = traceBuilderState.get();

        Integer constructorLevelFromState;
        int constructorLevel;

        constructorLevelFromState = state.leaveConstructorLevel.get(className);
        if (null == constructorLevelFromState) {
            constructorLevel = 0;
        } else {
            constructorLevel = constructorLevelFromState;
        }

        constructorLevel++;

        state.leaveConstructorLevel.put(className, constructorLevel);

        int enterConstructorLevel = state.enterConstructorLevel.get(className);
        int leaveConstructorLevel = state.leaveConstructorLevel.get(className);

//        System.out.println("enterConstructorLevel=" + enterConstructorLevel);
//        System.out.println("leaveConstructorLevel=" + leaveConstructorLevel);

        if (enterConstructorLevel < leaveConstructorLevel) {

//            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

            enter(
                    className,
                    methodName,
                    methodDescriptor,
                    null,
                    null);
            swapConstructors();
            leave();

        } else {
            leave();
        }

    }
}
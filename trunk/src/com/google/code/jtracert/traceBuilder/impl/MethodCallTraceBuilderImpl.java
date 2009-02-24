package com.google.code.jtracert.traceBuilder.impl;

import com.google.code.jtracert.config.AnalyzeProperties;
import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.traceBuilder.MethodCallTraceBuilder;
import com.google.code.jtracert.traceBuilder.impl.sdedit.SDEditFileClient;
import com.google.code.jtracert.traceBuilder.impl.sdedit.SDEditOutClient;
import com.google.code.jtracert.traceBuilder.impl.sdedit.SDEditRtClient;
import com.google.code.jtracert.traceBuilder.impl.sequence.SequenceFileClient;
import com.google.code.jtracert.traceBuilder.impl.sequence.SequenceOutClient;
import com.google.code.jtracert.traceBuilder.impl.serializable.SerializableTcpClient;
import com.google.code.jtracert.traceBuilder.impl.serializable.SerializableTcpServer;
import com.google.code.jtracert.traceBuilder.impl.webSequenceDiagrams.WebSequenceDiagramsFileClient;
import com.google.code.jtracert.traceBuilder.impl.webSequenceDiagrams.WebSequenceDiagramsOutClient;
import com.google.code.jtracert.traceBuilder.impl.graph.NormalizeMetodCallGraphVisitor;
import com.google.code.jtracert.traceBuilder.impl.graph.HashCodeBuilderMethodCallGraphVisitor;
import com.google.code.jtracert.util.FileUtils;
import com.google.code.jtracert.util.SizeOutputStream;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

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

/**
 * The default thread factory
 */
class JTracertThreadFactory implements ThreadFactory {

    static final AtomicInteger poolNumber = new AtomicInteger(1);
    final ThreadGroup group;
    final AtomicInteger threadNumber = new AtomicInteger(1);
    final String namePrefix;

    JTracertThreadFactory() {
        SecurityManager s = System.getSecurityManager();
        group = (s != null)? s.getThreadGroup() :
                             Thread.currentThread().getThreadGroup();
        namePrefix = "jTracert-" +
                      poolNumber.getAndIncrement() +
                     "-thread-";
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
                              namePrefix + threadNumber.getAndIncrement(),
                              0);
        t.setDaemon(true);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}

public class MethodCallTraceBuilderImpl implements MethodCallTraceBuilder {

    private static MethodCallTraceBuilderStateThreadLocal traceBuilderState = new MethodCallTraceBuilderStateThreadLocal();

    private ThreadPoolExecutor executorService;
    private Set<Integer> processedHashCodes;
    private static final int MAX_COUNT = 10000;

    public MethodCallTraceBuilderImpl() {

        processedHashCodes = new HashSet<Integer>();

        executorService = new ThreadPoolExecutor(
                1,
                1,
                1L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(5,true),
                new JTracertThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        Runtime.getRuntime().addShutdownHook(new Thread(
                new Runnable() {
                    public void run() {
                        executorService.shutdown();
                        try {
                            executorService.awaitTermination(5L, TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        ));

    }

    private Set<String> jarURLs = new HashSet<String>();

    public void enter(String className, String methodName, String methodDescriptor, Object object, Object[] arguments/*, JTracertObjectCompanion jTracertObjectCompanion*/) {

        //watchJar(object); // todo uncomment this line in order to track JAR dependencies

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

            if (state.count > MAX_COUNT) return;

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

            if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
                System.out.println(className + "." + methodName + methodDescriptor +  " <<<");
            }

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            state.buildingTrace = false;
        }

    }

    /**
     * @todo refactor this method in order to store dependency information in some storage
     */
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
                        case serializableTcpServer:
                            methodCallProcessor = SerializableTcpServer.getIstance();
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

            if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
                System.out.println(contextMethodCall.getRealClassName() + "." + contextMethodCall.getMethodName() + contextMethodCall.getMethodSignature() + " >>>");
            }

            if (1 == state.level) {

                if (state.count > MAX_COUNT) {
                    if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
                        System.out.println("Too large trace detected - will not be processed");
                    }
                } else {
                    graphFinished(contextMethodCall);
                }
                traceBuilderState.remove();
            } else {

                if (state.count > MAX_COUNT) return;

                state.methodCall = contextMethodCall.getCalleer();
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

        MethodCall parentMethodCall = currentMethodCall.getCalleer();

        if (null != parentMethodCall) {
            List<MethodCall> siblingCallees = parentMethodCall.getCallees();

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
    }

    public void leaveConstructor(String className, String methodName, String methodDescriptor, Throwable exception) {

        if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
            System.out.println();
            System.out.println(System.identityHashCode(exception));
            System.out.println();
            System.out.println("Leaving constructor on exception with desriptor " + methodDescriptor);
        }

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

        if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
            System.out.println("enterConstructorLevel=" + enterConstructorLevel);
            System.out.println("leaveConstructorLevel=" + leaveConstructorLevel);
        }

        if (enterConstructorLevel < leaveConstructorLevel) {

            if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
                System.out.println("enterConstructorLevel < leaveConstructorLevel");
            }

            enter(
                    className,
                    methodName,
                    methodDescriptor,
                    null,
                    null);


            // Assign hash code from previous sibling constructor

            MethodCall currentMethodCall = state.methodCall;

            List<MethodCall> siblingCallees = currentMethodCall.getCalleer().getCallees();

            if (siblingCallees.size() > 1) {
                MethodCall previousSiblingMethodCall = siblingCallees.get(siblingCallees.size() - 2);

                if (previousSiblingMethodCall.getMethodName().equals("<init>")) {
                    currentMethodCall.setObjectHashCode(previousSiblingMethodCall.getObjectHashCode());
                }

            }

            // EO assign hash code

            swapConstructors();
            leave();

        } else {
            leave();
        }

    }
}
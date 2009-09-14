package com.google.code.jtracert.agent;

import com.google.code.jtracert.config.AnalyzeProperties;
import com.google.code.jtracert.config.InstrumentationProperties;
import com.google.code.jtracert.traceBuilder.MethodCallTraceBuilderFactory;
import com.google.code.jtracert.traceBuilder.impl.serializable.SerializableTcpServer;
import com.google.code.jtracert.instrument.impl.adapter.JTracertByteCodeTransformerAdapter;
import com.google.code.jtracert.instrument.JTracertByteCodeTransformerFactory;
import com.google.code.jtracert.instrument.JTracertByteCodeTransformer;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.ClassDefinition;
import java.util.jar.JarFile;
import java.net.URL;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Security;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class JTracertAgent {

    /**
     * @param arg arguments passed to the agent; port address currently
     * @param instrumentation instrumentation instance
     */
    public static void premain(final String arg, Instrumentation instrumentation) {

        System.out.println();
        System.out.println("jTracert agent started");
        System.out.println("agent version: " + getAgentVersion());

        //appendJTracertToBootstrapClassLoaderSearch(instrumentation);

        InstrumentationProperties instrumentationProperties =
                InstrumentationProperties.loadFromSystemProperties();

        retransformSystemClasses = instrumentationProperties.isTransformSystemClasses();

        //redefineLoadedClasses(instrumentation, instrumentationProperties);

        JTracertClassFileTransformer jTracertClassFileTransformer =
                new JTracertClassFileTransformer(instrumentationProperties);

        AnalyzeProperties analyzeProperties =
                AnalyzeProperties.loadFromSystemProperties();

        switch (analyzeProperties.getAnalyzerOutput()) {
            case sdEditOut:
            case sdEditRtClient:
            case sdEditFileSystem:
            case sequenceOut:
            case sequenceFileSystem:
            case webSequenceDiagramsOut:
            case webSequenceDiagramsFileSystem:
                System.out.println();
                System.out.println("WARNING! You have selected deprecated analyzer output format!");
                System.out.println("It will be removed in further versions of jTracert!");
                System.out.println();
                break;
        }

        if ((null != arg) && (!"".equals(arg))) {

            try {
                processJTracertGuiConnection(arg, instrumentationProperties, analyzeProperties);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }

        MethodCallTraceBuilderFactory.
                configureMethodCallTraceBuilder(analyzeProperties, instrumentation);

        instrumentation.
                addTransformer(jTracertClassFileTransformer);

        //setNativeMethodPrefix(instrumentation, jTracertClassFileTransformer);


        System.out.println();

    }

    private static boolean retransformSystemClasses;

    public static boolean isRetransformSystemClasses() {
        return retransformSystemClasses;
    }

    private static String getAgentVersion() {
        return "0.1.3";
    }

    @Deprecated
    private static void redefineLoadedClasses(Instrumentation instrumentation, InstrumentationProperties instrumentationProperties) {
        InputStream objectClassInputStream =
                ClassLoader.getSystemResourceAsStream("java/lang/Object.class");

        InputStream systemClassInputStream =
                ClassLoader.getSystemResourceAsStream("java/lang/System.class");

        JTracertByteCodeTransformer jTracertByteCodeTransformer =
                JTracertByteCodeTransformerFactory.getJTracertByteCodeTransformer(
                        instrumentationProperties);

        JTracertByteCodeTransformerAdapter jTracertByteCodeTransformerAdapter =
                new JTracertByteCodeTransformerAdapter(jTracertByteCodeTransformer);

        try {
            byte[] transformedObjectByteCode =
                    jTracertByteCodeTransformerAdapter.transform(objectClassInputStream, true);

            byte[] transformedSystemByteCode =
                    jTracertByteCodeTransformerAdapter.transform(systemClassInputStream, true);

            instrumentation.redefineClasses(
                    new ClassDefinition(Object.class, transformedObjectByteCode)
                    //,new ClassDefinition(System.class, transformedSystemByteCode)
            );

        } catch (Exception e) {
            e.printStackTrace();
            Runtime.getRuntime().halt(-1);
        }
    }

    @Deprecated
    private static void setNativeMethodPrefix(Instrumentation instrumentation, JTracertClassFileTransformer jTracertClassFileTransformer) {
        try {
            if (instrumentation.isNativeMethodPrefixSupported()) {
                instrumentation.setNativeMethodPrefix(jTracertClassFileTransformer, "$jTracert$");
            }
        } catch (NoSuchMethodError r) {
            System.err.println("WARNING - Cannot set native method prefix; native methods will be absent in jTracet diagrams; use JRE 1.6+");
        }
    }

    @Deprecated
    private static void appendJTracertToBootstrapClassLoaderSearch(Instrumentation instrumentation) {
        try {
            URL agentJarLocation = JTracertAgent.class.getProtectionDomain().getCodeSource().getLocation();
            instrumentation.appendToBootstrapClassLoaderSearch(new JarFile(agentJarLocation.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchMethodError r) {
            System.err.println("WARNING - Cannot append jTracert agent to bootstrap class loader class path; some applications (OSGi for example) can be instrumented incorectly; use JRE 1.6+");
        }
    }

    /**
     * @param arg
     * @param instrumentationProperties
     * @param analyzeProperties
     * @throws InterruptedException
     */
    private static void processJTracertGuiConnection(String arg, InstrumentationProperties instrumentationProperties, AnalyzeProperties analyzeProperties) throws InterruptedException {

        int port = Integer.parseInt(arg);

        analyzeProperties.setAnalyzerOutput(AnalyzeProperties.AnalyzerOutput.serializableTcpServer);
        analyzeProperties.setSerializableTcpServerPort(port);

        System.out.println("Waiting for a connection from jTracert GUI on port " + port);

        SerializableTcpServer serializableTcpServer =
                SerializableTcpServer.getIstance(port);

        synchronized (serializableTcpServer) {
            while (!serializableTcpServer.connected) {
                serializableTcpServer.wait();
            }
        }

        InstrumentationProperties guiInstrumentationProperties =
                serializableTcpServer.getInstrumentationProperties();

        String guiClassNameRegEx = guiInstrumentationProperties.getClassNameRegEx();

        if ((null != guiClassNameRegEx) && (!"".equals(guiClassNameRegEx.trim()))) {
            System.out.println("Using class name filter: " + guiClassNameRegEx);
            instrumentationProperties.setClassNameRegEx(guiClassNameRegEx);
        }

    }

}
package com.google.code.jtracert.agent;

import com.google.code.jtracert.config.AnalyzeProperties;
import com.google.code.jtracert.config.InstrumentationProperties;
import com.google.code.jtracert.traceBuilder.MethodCallTraceBuilderFactory;
import com.google.code.jtracert.traceBuilder.impl.serializable.SerializableTcpServer;
import com.google.code.jtracert.instrument.JTracertByteCodeTransformer;
import com.google.code.jtracert.instrument.JTracertByteCodeTransformerFactory;
import com.google.code.jtracert.instrument.impl.adapter.JTracertByteCodeTransformerAdapter;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.ClassDefinition;
import java.io.InputStream;
import java.net.URL;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class JTracertAgent {

    /**
     * @param arg
     * @param instrumentation
     */
    public static void premain(final String arg, Instrumentation instrumentation) {

        System.out.println();
        System.out.println("jTracert agent started");

        InstrumentationProperties instrumentationProperties =
                InstrumentationProperties.loadFromSystemProperties();

        /*InputStream objectClassInputStream =
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
        }*/

        JTracertClassFileTransformer jTracertClassFileTransformer =
                new JTracertClassFileTransformer(instrumentationProperties);

        AnalyzeProperties analyzeProperties =
                AnalyzeProperties.loadFromSystemProperties();

        if ((null != arg) && (!"".equals(arg))) {

            try {
                processJTracertGuiConnection(arg, instrumentationProperties, analyzeProperties);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }

        MethodCallTraceBuilderFactory.
                configureMethodCallTraceBuilder(analyzeProperties);



        /*if (isRetransformSystemClasses()) {
            try {
                retransformSystemClasses(instrumentation, jTracertClassFileTransformer);
                //retransformSystemClasses(instrumentation);
            } catch (UnmodifiableClassException e) {
                e.printStackTrace(System.err); // todo refactor this line
            }
        }*/

        instrumentation.
                addTransformer(jTracertClassFileTransformer);

        /*try {
            if (instrumentation.isNativeMethodPrefixSupported()) {
                instrumentation.setNativeMethodPrefix(jTracertClassFileTransformer, "$jTracert$");
            }
        } catch (NoSuchMethodError r) {
            System.err.println("WARNING - Cannot set native method prefix; native methods will be absent in jTracet diagrams; use JRE 1.6+");
        }*/

        System.out.println();

    }

    @Deprecated
    public static boolean isRetransformSystemClasses() {
        return false;
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
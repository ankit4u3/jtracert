package com.google.code.jtracert.agent;

import com.google.code.jtracert.config.AnalyzeProperties;
import static com.google.code.jtracert.config.AnalyzeProperties.AnalyzerOutput.serializableTcpServer;
import com.google.code.jtracert.config.InstrumentationProperties;
import com.google.code.jtracert.traceBuilder.MethodCallTraceBuilderFactory;
import com.google.code.jtracert.traceBuilder.impl.serializable.SerializableTcpServer;

import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;
import java.io.IOException;
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

        try {
            URL agentJarLocation = JTracertAgent.class.getProtectionDomain().getCodeSource().getLocation();
            instrumentation.appendToBootstrapClassLoaderSearch(new JarFile(agentJarLocation.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchMethodError r) {
            System.err.println("WARNING - Cannot append jTracert agent to bootstrap class loader class path; some applications (OSGi for example) can be instrumented incorectly; use JRE 1.6+");
        }

        InstrumentationProperties instrumentationProperties =
                InstrumentationProperties.loadFromSystemProperties();

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

        instrumentation.
                addTransformer(jTracertClassFileTransformer);

        try {
            instrumentation.setNativeMethodPrefix(jTracertClassFileTransformer, "$jTracert$");
        } catch (NoSuchMethodError r) {
            System.err.println("WARNING - Cannot set native method prefix; native methods will be absent in jTracet diagrams; use JRE 1.6+");
        }

        System.out.println();

    }

    /**
     * @param arg
     * @param instrumentationProperties
     * @param analyzeProperties
     * @throws InterruptedException
     */
    private static void processJTracertGuiConnection(String arg, InstrumentationProperties instrumentationProperties, AnalyzeProperties analyzeProperties) throws InterruptedException {

        int port = Integer.parseInt(arg);

        analyzeProperties.setAnalyzerOutput(serializableTcpServer);
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
            instrumentationProperties.setClassNameRegEx(guiClassNameRegEx);
        }

    }

}
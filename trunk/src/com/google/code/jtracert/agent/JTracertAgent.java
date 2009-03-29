package com.google.code.jtracert.agent;

import com.google.code.jtracert.config.AnalyzeProperties;
import static com.google.code.jtracert.config.AnalyzeProperties.AnalyzerOutput.serializableTcpServer;
import com.google.code.jtracert.config.InstrumentationProperties;
import com.google.code.jtracert.traceBuilder.MethodCallTraceBuilderFactory;
import com.google.code.jtracert.traceBuilder.impl.serializable.SerializableTcpServer;
import com.google.code.jtracert.util.ClassUtils;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.instrument.ClassDefinition;
import java.util.jar.JarFile;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
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



        if (isRetransformSystemClasses()) {
            try {
                retransformSystemClasses(instrumentation, jTracertClassFileTransformer);
                //retransformSystemClasses(instrumentation);
            } catch (UnmodifiableClassException e) {
                e.printStackTrace(System.err); // todo refactor this line
            }
        }

        instrumentation.
                addTransformer(jTracertClassFileTransformer);

        try {
            if (instrumentation.isNativeMethodPrefixSupported()) {
                instrumentation.setNativeMethodPrefix(jTracertClassFileTransformer, "$jTracert$");
            }
        } catch (NoSuchMethodError r) {
            System.err.println("WARNING - Cannot set native method prefix; native methods will be absent in jTracet diagrams; use JRE 1.6+");
        }

        System.out.println();

    }

    public static boolean isRetransformSystemClasses() {
        return false;
    }

    private static void retransformSystemClasses(Instrumentation instrumentation,
                                                 JTracertClassFileTransformer jTracertClassFileTransformer)
            throws UnmodifiableClassException {

        /*try {
            if (instrumentation.isRetransformClassesSupported()) {
                Class[] loadedClasses = instrumentation.getAllLoadedClasses();
                List<Class> classesToBeRetransformed = new ArrayList<Class>(loadedClasses.length);
                for (Class clazz : loadedClasses) {
                    if (instrumentation.isModifiableClass(clazz)) {
                        classesToBeRetransformed.add(clazz);
                    }
                }
                instrumentation.retransformClasses(classesToBeRetransformed.toArray(new Class<?>[]{null}));
            }
        } catch (NoSuchMethodError r) {
            System.err.println("WARNING - Cannot retransform system classes; these classes will be absent in jTracert analyzis; use JRE 1.6+");
        }*/

        for (Class clazz : instrumentation.getAllLoadedClasses()) {

            if ("java.lang.Integer".equals(clazz.getName())) continue;
            if ("java.lang.Long".equals(clazz.getName())) continue;

            String classFileName = ClassUtils.convertClassNameToResourceName(clazz.getName());
            if (classFileName.charAt(0) == '[') continue;

            System.out.println("ClassLoader.getSystemResource(classFileName)=" + ClassLoader.getSystemResource(classFileName));

            InputStream inputStream = ClassLoader.getSystemResourceAsStream(classFileName);
            ByteArrayOutputStream originalByteArrayOutputStream = new ByteArrayOutputStream();

            byte[] originalBytes = null;

            try {

                while (inputStream.available() > 0) {
                    originalByteArrayOutputStream.write(inputStream.read());
                }

                originalBytes = originalByteArrayOutputStream.toByteArray();

            } catch (IOException e) {
                e.printStackTrace(); // todo refactor this line
            } finally {
                try {
                    originalByteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace(); // todo refactor this line
                }
            }

            if (null != originalBytes) {

                try {
                    byte[] transformedBytes = jTracertClassFileTransformer.transform(
                            clazz.getClassLoader(),
                            clazz.getName(),
                            clazz,
                            clazz.getProtectionDomain(),
                            originalBytes
                    );

                    if (null != transformedBytes) {
                        ClassDefinition retransformedClassDefinition = new ClassDefinition(clazz, transformedBytes);
                        instrumentation.redefineClasses(retransformedClassDefinition);
                    }
                    
                } catch (Throwable e) {
                    e.printStackTrace();
                }

            }

        }

        System.out.println("Done!");

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
            System.out.println("Using class name filter: " + guiClassNameRegEx);
            instrumentationProperties.setClassNameRegEx(guiClassNameRegEx);
        }

    }

}
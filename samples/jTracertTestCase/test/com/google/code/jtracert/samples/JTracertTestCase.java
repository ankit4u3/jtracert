package com.google.code.jtracert.samples;

import com.google.code.jtracert.model.MethodCall;
import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public abstract class JTracertTestCase extends TestCase {

    protected final String PATH_SEPARATOR = System.getProperty("path.separator");
    protected final String FILE_SEPARATOR = System.getProperty("file.separator");

    protected Process startJavaProcessWithJTracert(String jarFileName) throws IOException {
        return startJavaProcessWithJTracert(jarFileName, false);
    }

    protected Process startJavaProcessWithJTracert(String jarFileName, boolean verbose) throws IOException {

        String[] commands =
                verbose ?
                        new String[]{
                                getJava(),
                                "-DanalyzerOutput=serializableTcpClient",
                                "-DdumpTransformedClasses",
                                "-DverboseInstrumentation=true",
                                "-DverboseAnalyze=true",
                                "-javaagent:../../deploy/jTracert.jar",
                                "-jar",jarFileName
                        }
                        :
                        new String[]{
                                getJava(),
                                "-DanalyzerOutput=serializableTcpClient",
                                "-DdumpTransformedClasses",
                                "-javaagent:../../deploy/jTracert.jar",
                                "-jar",jarFileName
                        };

        if (verbose) {
            System.out.println(Arrays.toString(commands));
        }

        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.redirectErrorStream(true);
        File directory = new File(".").getAbsoluteFile();
        processBuilder.directory(directory);

        Process process = processBuilder.start();

        InputStream is = process.getInputStream();
        for(int i = is.read(); i != -1; i = is.read())
        {
            System.out.print((char)i);
        }

        return process;

    }

    protected String getJava() {
        String javaHome = System.getenv("JAVA_HOME");
        if (null == javaHome) {
            return "java";
        } else {
            return javaHome + FILE_SEPARATOR + "bin" + FILE_SEPARATOR + "java"; 
        }
    }

    protected JTracertSerializableTcpServer startJTracertTcpServer(int port) {
        JTracertSerializableTcpServer tcpServer = new JTracertSerializableTcpServer(port);
        tcpServer.start();
        return tcpServer;
    }

    protected void dumpMethodCall(MethodCall methodCall) {
        dumpMethodCall(methodCall, 0);
    }

    private void dumpMethodCall(MethodCall methodCall, int pad) {
        for (int i = 0; i < pad; i++) System.out.print(" ");
        System.out.println(methodCall.getRealClassName() + "." + methodCall.getMethodName() + methodCall.getMethodSignature() + " callCount=" + methodCall.getCallCount() + " #" + methodCall.getObjectHashCode());
        for (MethodCall callee : methodCall.getCallees()) {
            dumpMethodCall(callee, pad + 1);
        }
    }

    protected Process startJavaProcessWithJTracert(String jarFileName, Collection<String> classpath) throws IOException {
        return startJavaProcessWithJTracert(jarFileName, classpath, false);
    }

    protected Process startJavaProcessWithJTracert(String jarFileName, Collection<String> classpath, boolean verbose) throws IOException {

        boolean classpathSpecified = false;

        if ((null != classpath) && (!classpath.isEmpty())) {
            classpathSpecified = true;
        }

        ProcessBuilder processBuilder;

        if (classpathSpecified) {

            JarFile jarFile = new JarFile(jarFileName);
            Manifest manifest = jarFile.getManifest();

            String className = manifest.getMainAttributes().getValue("Main-Class");

            String[] commands;

            if (verbose) {
                commands = new String[]{
                        getJava(),
                        "-DanalyzerOutput=serializableTcpClient",
                        "-DverboseInstrumentation=true",
                        "-DverboseAnalyze=true",
                        "-javaagent:../../deploy/jTracert.jar",
                        className
                };
            } else {
                commands = new String[]{
                        getJava(),
                        "-DanalyzerOutput=serializableTcpClient",
                        "-javaagent:../../deploy/jTracert.jar",
                        className
                };
            }

            if (verbose) {
                System.out.println(Arrays.toString(commands));
            }

            processBuilder = new ProcessBuilder(commands);

            classpath = new HashSet<String>(classpath);
            classpath.add(jarFileName);

            Appendable classpathBuilder = new StringBuffer();
            boolean firstEntry = true;

            for (String classpathEntry : classpath) {
                if (!firstEntry) {
                    classpathBuilder.append(PATH_SEPARATOR);
                }
                firstEntry = false;
                classpathBuilder.append(classpathEntry);
            }

            String classpathString = classpathBuilder.toString();

            processBuilder.environment().put("CLASSPATH",classpathString);

        } else {
            
            String[] commands;

            if (verbose) {
                commands = new String[]{
                        getJava(),
                        "-DanalyzerOutput=serializableTcpClient",
                        "-DverboseInstrumentation=true",
                        "-DverboseAnalyze=true",
                        "-javaagent:../../deploy/jTracert.jar",
                        "-jar",jarFileName
                };
            } else {
                commands = new String[]{
                        getJava(),
                        "-DanalyzerOutput=serializableTcpClient",
                        "-javaagent:../../deploy/jTracert.jar",
                        "-jar",jarFileName
                };
            }

            if (verbose) {
                System.out.println(Arrays.toString(commands));
            }
            
            processBuilder = new ProcessBuilder(commands);
        }

        processBuilder.redirectErrorStream(true);
        File directory = new File(".").getAbsoluteFile();
        processBuilder.directory(directory);

        Process process = processBuilder.start();

        InputStream is = process.getInputStream();
        for(int i = is.read(); i != -1; i = is.read())
        {
            System.out.print((char)i);
        }

        return process;

    }
}

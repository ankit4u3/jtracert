package com.google.code.jtracert.samples;

import com.google.code.jtracert.model.MethodCall;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class XercesTest extends JTracertTestCase {

    protected Process startJavaProcessWithJTracert(String jarFileName, Collection<String> classpath) throws IOException {

        boolean classpathSpecified = false;

        if ((null != classpath) && (!classpath.isEmpty())) {
            classpathSpecified = true;
        }

        ProcessBuilder processBuilder;

        if (classpathSpecified) {

            JarFile jarFile = new JarFile(jarFileName);
            Manifest manifest = jarFile.getManifest();

            String className = manifest.getMainAttributes().getValue("Main-Class");

            String[] commands = new String[]{
                    "java",
                    "-DanalyzerOutput=serializableTcpClient",
                    "-DverboseInstrumentation=true",
                    "-DverboseAnalyze=true",
                    "-javaagent:../../deploy/jTracert.jar",
                    className
            };

            processBuilder = new ProcessBuilder(commands);

            classpath = new HashSet<String>(classpath);
            classpath.add(jarFileName);

            String pathSeparator = System.getProperty("path.separator");

            Appendable classpathBuilder = new StringBuffer();
            boolean firstEntry = true;

            for (String classpathEntry : classpath) {
                if (!firstEntry) {
                    classpathBuilder.append(pathSeparator);
                }
                firstEntry = false;
                classpathBuilder.append(classpathEntry);
            }

            String classpathString = classpathBuilder.toString();

            processBuilder.environment().put("CLASSPATH",classpathString);
            
        } else {
            String[] commands = new String[]{
                    "java",
                    "-DanalyzerOutput=serializableTcpClient",
                    "-DverboseInstrumentation=true",
                    "-DverboseAnalyze=true",
                    "-javaagent:../../deploy/jTracert.jar",
                    "-jar",jarFileName
            };

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

    @Test
    public void testXerces() throws Exception {

        try {
            JTracertSerializableTcpServer tcpServer = startJTracertTcpServer(60002);

            Process process = startJavaProcessWithJTracert("deploy/xerces.jar", Arrays.asList("lib/xercesImpl-2.4.0.jar"));

            int exitCode = process.waitFor();

            assertEquals(0, exitCode);

            MethodCall methodCall = tcpServer.getMethodCall();

            assertNotNull(methodCall);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


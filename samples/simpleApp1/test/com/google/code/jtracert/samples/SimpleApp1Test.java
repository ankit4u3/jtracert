package com.google.code.jtracert.samples;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.*;

import com.google.code.jtracert.model.MethodCall;

public class SimpleApp1Test extends TestCase {

    @Test
    public void testSimpleApp1() throws Exception {

        JTracertSerializableTcpServer tcpServer = new JTracertSerializableTcpServer(60002);
        tcpServer.start();

        Process process = startJavaProcessWithJTracert("deploy/simpleApp1.jar");

        int exitCode = process.waitFor();

        assertEquals(0, exitCode);

        MethodCall methodCall = tcpServer.getMethodCall();

        assertNotNull(methodCall);

    }

    private Process startJavaProcessWithJTracert(String jarFileName) throws IOException {

        String[] commands = new String[]{
                "java",
                "-DanalyzerOutput=serializableTcpClient",
                "-javaagent:../../deploy/jTracert.jar",
                "-jar",jarFileName
        };

        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.redirectErrorStream(true);
        processBuilder.directory(new File(".").getAbsoluteFile());

        return processBuilder.start();
    }

}

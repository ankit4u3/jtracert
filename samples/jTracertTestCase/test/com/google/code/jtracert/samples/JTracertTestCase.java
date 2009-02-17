package com.google.code.jtracert.samples;

import junit.framework.TestCase;

import java.io.IOException;
import java.io.File;
import java.io.InputStream;

import com.google.code.jtracert.model.MethodCall;

public abstract class JTracertTestCase extends TestCase {

    protected Process startJavaProcessWithJTracert(String jarFileName) throws IOException {

        String[] commands = new String[]{
                "java",
                "-DanalyzerOutput=serializableTcpClient",
                "-DdumpTransformedClasses",
                "-javaagent:../../deploy/jTracert.jar",
                "-jar",jarFileName
        };

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
    
}

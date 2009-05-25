package com.google.code.jtracert.samples;

import org.junit.Test;
import com.google.code.jtracert.model.MethodCall;

import java.util.Arrays;

public class Issue15Test extends JTracertTestCase {

    @Test
    public void testIssue15() throws Exception {

        JTracertSerializableTcpServer tcpServer = startJTracertTcpServer(60002);

        Process process = startJavaProcessWithJTracert(
                "deploy/issue15.jar",
                Arrays.asList("lib/McCabeAnalyzer.jar"));

        int exitCode = process.waitFor();

        assertEquals(0, exitCode);

        MethodCall methodCall = tcpServer.getMethodCall();

        assertNotNull(methodCall);

        assertEquals("Issue15",methodCall.getClassName());
        assertEquals("main",methodCall.getMethodName());

        methodCall = methodCall.getCallees().get(0);

        assertEquals("Parser",methodCall.getClassName());
        assertEquals("<init>",methodCall.getMethodName());

    }

}


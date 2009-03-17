package com.google.code.jtracert.samples;

import org.junit.Test;
import com.google.code.jtracert.model.MethodCall;

public class Issue13Test extends JTracertTestCase {

    @Test
    public void testIssue13() throws Exception {

        JTracertSerializableTcpServer tcpServer = startJTracertTcpServer(60002);

        Process process = startJavaProcessWithJTracert("deploy/issue13.jar");

        int exitCode = process.waitFor();

        assertEquals(0, exitCode);

        MethodCall methodCall = tcpServer.getMethodCall();

        assertNotNull(methodCall);

        dumpMethodCall(methodCall);

    }

}


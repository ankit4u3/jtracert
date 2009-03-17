package com.google.code.jtracert.samples;

import com.google.code.jtracert.model.MethodCall;
import org.junit.Test;

public class SimpleApp1Test extends JTracertTestCase {

    @Test
    public void testSimpleApp1() throws Exception {

        JTracertSerializableTcpServer tcpServer = startJTracertTcpServer(60002);

        Process process = startJavaProcessWithJTracert("deploy/simpleApp1.jar", false);

        int exitCode = process.waitFor();

        assertEquals(0, exitCode);

        MethodCall methodCall = tcpServer.getMethodCall();

        assertNotNull(methodCall);

        assertEquals("main",methodCall.getMethodName());

    }

}

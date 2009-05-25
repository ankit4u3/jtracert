package com.google.code.jtracert.samples;

import org.junit.Test;
import com.google.code.jtracert.model.MethodCall;

public class ConstructorTest extends JTracertTestCase {

    @Test
    public void testConstructor() throws Exception {

        JTracertSerializableTcpServer tcpServer = startJTracertTcpServer(60002);

        Process process = startJavaProcessWithJTracert("deploy/constructor.jar", true);

        int exitCode = process.waitFor();

        assertEquals(0, exitCode);

        MethodCall methodCall = tcpServer.getMethodCall();

        assertNotNull(methodCall);

    }

}


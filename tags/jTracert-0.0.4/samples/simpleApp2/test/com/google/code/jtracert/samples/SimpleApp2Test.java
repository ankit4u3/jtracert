package com.google.code.jtracert.samples;

import org.junit.Test;
import com.google.code.jtracert.model.MethodCall;

import java.io.FileWriter;

public class SimpleApp2Test extends JTracertTestCase {

    @Test
    public void testSimpleApp2() throws Exception {

        JTracertSerializableTcpServer tcpServer = startJTracertTcpServer(60002);

        Process process = startJavaProcessWithJTracert("deploy/simpleApp2.jar");

        int exitCode = process.waitFor();

        assertEquals(0, exitCode);

        MethodCall methodCall = tcpServer.getMethodCall();

        assertNotNull(methodCall);

        String className = methodCall.getCallees().get(1).getRealClassName();
        assertEquals("com.google.code.jtracert.samples.SimpleApp2$SimpleApp2InnerClass1", className);

    }

}


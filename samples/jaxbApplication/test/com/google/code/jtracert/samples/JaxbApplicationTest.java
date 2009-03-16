package com.google.code.jtracert.samples;

import com.google.code.jtracert.model.MethodCall;
import org.junit.Test;

import java.util.Arrays;

public class JaxbApplicationTest extends JTracertTestCase {

    @Test
    public void testJaxbApplication() throws Exception {

        JTracertSerializableTcpServer tcpServer = startJTracertTcpServer(60002);

        Process process = startJavaProcessWithJTracert("deploy/jaxbApplication.jar",
                Arrays.asList("lib/activation.jar","lib/jaxb-api.jar","lib/jaxb-impl.jar","lib/jsr173_api.jar"), true);

        int exitCode = process.waitFor();

        assertEquals(0, exitCode);

        MethodCall methodCall = tcpServer.getMethodCall();

        assertNotNull(methodCall);

    }

}


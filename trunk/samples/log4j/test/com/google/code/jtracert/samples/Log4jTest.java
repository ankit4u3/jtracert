package com.google.code.jtracert.samples;

import org.junit.Test;
import com.google.code.jtracert.model.MethodCall;

import java.util.Arrays;

public class Log4jTest extends JTracertTestCase {

    @Test
    public void testLog4j() throws Exception {

        JTracertSerializableTcpServer tcpServer = startJTracertTcpServer(60002);

        Process process = startJavaProcessWithJTracert("deploy/log4j.jar",
                Arrays.asList("lib/log4j-1.2.15.jar"));

        int exitCode = process.waitFor();

        assertEquals(0, exitCode);

        MethodCall methodCall = tcpServer.getMethodCall();

        assertNotNull(methodCall);

    }

}


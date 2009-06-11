package com.google.code.jtracert.samples;

import org.junit.Test;
import com.google.code.jtracert.model.MethodCall;

import java.util.Arrays;

public class XalanTest extends JTracertTestCase {

    @Test
    public void testXalan() throws Exception {

        JTracertSerializableTcpServer tcpServer = startJTracertTcpServer(60002);

        Process process = startJavaProcessWithJTracert(
                "deploy/xalan.jar",
                Arrays.asList("lib/xercesImpl.jar","lib/xml-apis.jar","lib/xsltc.jar","lib/serializer.jar")
        );

        int exitCode = process.waitFor();

        assertEquals(0, exitCode);

        MethodCall methodCall = tcpServer.getMethodCall();

        assertNotNull(methodCall);

    }

}


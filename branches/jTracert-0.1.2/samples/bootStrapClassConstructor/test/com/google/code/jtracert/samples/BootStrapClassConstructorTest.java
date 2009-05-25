package com.google.code.jtracert.samples;

import org.junit.Test;
import com.google.code.jtracert.model.MethodCall;

public class BootStrapClassConstructorTest extends JTracertTestCase {

    @Test
    public void testBootStrapClassConstructor() throws Exception {

        try {

            JTracertSerializableTcpServer tcpServer = startJTracertTcpServer(60002);

            Process process = startJavaProcessWithJTracert("deploy/bootStrapClassConstructor.jar");

            int exitCode = process.waitFor();

            assertEquals(0, exitCode);

            MethodCall methodCall = tcpServer.getMethodCall();

            assertNotNull(methodCall);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


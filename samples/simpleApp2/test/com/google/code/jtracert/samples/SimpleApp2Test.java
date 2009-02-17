package com.google.code.jtracert.samples;

import com.google.code.jtracert.model.MethodCall;
import org.junit.Test;

public class SimpleApp2Test extends JTracertTestCase {

    @Test
    public void testSimpleApp2() throws Exception {

        try {
            JTracertSerializableTcpServer tcpServer = startJTracertTcpServer(60002);

            Process process = startJavaProcessWithJTracert("deploy/simpleApp2.jar");

            int exitCode = process.waitFor();

            assertEquals(0, exitCode);

            MethodCall methodCall = tcpServer.getMethodCall();

            assertNotNull(methodCall);

            dumpMethodCall(methodCall);
            String className = methodCall.getCallees().get(0).getRealClassName();
            assertEquals("com.google.code.jtracert.samples.SimpleApp2$SimpleApp2InnerClass1", className);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

}


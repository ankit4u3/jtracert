package com.google.code.jtracert.samples;

import com.google.code.jtracert.model.MethodCall;
import org.junit.Test;

import java.util.Arrays;

public class XercesTest extends JTracertTestCase {

    @Test
    public void testXerces() throws Exception {

        try {
            JTracertSerializableTcpServer tcpServer = startJTracertTcpServer(60002);

            Process process = startJavaProcessWithJTracert(
                    "deploy/xerces.jar",
                    Arrays.asList("lib/xercesImpl-2.4.0.jar"),
                    Arrays.asList("-DmaximalTraceLength=10000","-Xmx512m"),
                    false);

            int exitCode = process.waitFor();

            assertEquals(0, exitCode);

            MethodCall methodCall = tcpServer.getMethodCall();

            assertNotNull(methodCall);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

}


package com.google.code.jtracert.samples;

import org.junit.Test;
import com.google.code.jtracert.model.MethodCall;

public class EquinoxTest extends JTracertTestCase {

    private static final Integer ZERO = 0;

    @Test
    public void testEquinox() throws Exception {

        JTracertSerializableTcpServer tcpServer = startJTracertTcpServer(60002);

        final Process process = startJavaProcessWithJTracert(
                "deploy/org.eclipse.osgi_3.4.2.R34x_v20080826-1230.jar",
                "com\\.google\\.code\\.jtracert.*");
        
        try {
            Integer exitCode = null;

            Thread.sleep(1000);

            try {
                exitCode = process.exitValue();
            } catch (IllegalThreadStateException e1) {
                e1.printStackTrace();
                fail();
            }


            assertEquals(ZERO, exitCode);

            MethodCall methodCall = tcpServer.getMethodCall();

            assertNotNull(methodCall);

            dumpMethodCall(methodCall);
        }
        finally {
            process.destroy();
        }

    }

}
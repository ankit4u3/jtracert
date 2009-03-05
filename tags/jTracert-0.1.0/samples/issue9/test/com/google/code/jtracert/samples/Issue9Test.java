package com.google.code.jtracert.samples;

import com.google.code.jtracert.model.MethodCall;
import org.junit.Test;

public class Issue9Test extends JTracertTestCase {

    @Test
    public void testIssue9() throws Exception {

        JTracertSerializableTcpServer tcpServer = startJTracertTcpServer(60002);

        Process process = startJavaProcessWithJTracert("deploy/issue9.jar");

        int exitCode = process.waitFor();

        assertEquals(0, exitCode);

        MethodCall methodCall;

        methodCall = tcpServer.getMethodCall();

        assertNotNull(methodCall);

        assertEquals("run",methodCall.getMethodName());
        assertEquals(1,methodCall.getCallees().size());
        assertEquals("(I)V",methodCall.getCallees().get(0).getMethodSignature());
        assertEquals(1,methodCall.getCallees().get(0).getCallees().size());
        assertEquals("(II)V",methodCall.getCallees().get(0).getCallees().get(0).getMethodSignature());
        assertEquals(1,methodCall.getCallees().get(0).getCallees().get(0).getCallees().size());
        assertEquals("(III)V",methodCall.getCallees().get(0).getCallees().get(0).getCallees().get(0).getMethodSignature());

        methodCall = tcpServer.getMethodCall();

        assertNotNull(methodCall);

        assertEquals("main",methodCall.getMethodName());
        assertEquals(2,methodCall.getCallees().size());
        assertEquals("()V",methodCall.getCallees().get(0).getMethodSignature());
        assertEquals(1,methodCall.getCallees().get(0).getCallees().size());
        assertEquals("(Ljava/lang/String;)V",methodCall.getCallees().get(0).getCallees().get(0).getMethodSignature());

    }

}


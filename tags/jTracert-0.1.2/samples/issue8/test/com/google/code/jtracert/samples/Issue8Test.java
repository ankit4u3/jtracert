package com.google.code.jtracert.samples;

import com.google.code.jtracert.model.MethodCall;
import org.junit.Test;

import java.util.Comparator;

public class Issue8Test extends JTracertTestCase {

    @Test
    public void test() throws Exception {

        JTracertSerializableTcpServer tcpServer = startJTracertTcpServer(60002);

        Process process = startJavaProcessWithJTracert("deploy/issue8.jar");

        int exitCode = process.waitFor();

        assertEquals(0, exitCode);

        MethodCall methodCall = tcpServer.getMethodCall();

        assertNotNull(methodCall);
        assertEquals(2,methodCall.getCallees().size());

        MethodCall methodCall1 = methodCall.getCallees().get(0);
        MethodCall methodCall2 = methodCall.getCallees().get(1);

        Comparator<MethodCall> mcComparator = new MethodCallComparator();

        methodCall2.setMethodName(methodCall1.getMethodName());
        int compareResult = mcComparator.compare(methodCall1, methodCall2);

        assertEquals(0, compareResult);

    }

    private static class MethodCallComparator implements Comparator<MethodCall> {

        public int compare(MethodCall mc1, MethodCall mc2) {

            if (null == mc1) {
                return (null == mc2) ? 0 : -1;
            }

            if (null == mc2) {
                return 1;
            }

            int result;

            result = mc1.getRealClassName().compareTo(mc2.getRealClassName());

            if (0 != result) {
                return result;
            }

            result = mc1.getMethodName().compareTo(mc2.getMethodName());

            if (0 != result) {
                return result;
            }

            result = mc1.getMethodSignature().compareTo(mc2.getMethodSignature());

            if (0 != result) {
                return result;
            }

            result = mc2.getCallees().size() - mc1.getCallees().size();

            if (0 != result) {
                return result;
            }

            for (int i = 0; i < mc1.getCallees().size(); i++) {

                result = compare(mc1.getCallees().get(i), mc2.getCallees().get(i));
                
                if (0 != result) {
                    return result;
                }

            }

            return result;

        }

    }


}


package com.google.code.jtracert.samples;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.*;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.code.jtracert.model.MethodCall;

public class SimpleApp1Test extends TestCase {

    private static class SerializableTcpServer implements Runnable {

        private Queue<MethodCall> methodCallsQueue = new ArrayBlockingQueue<MethodCall>(1);

        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(60002);
                Socket socket = serverSocket.accept();

                InputStream inputStream = socket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                Object object = objectInputStream.readObject();

                MethodCall methodCall = (MethodCall) object;

                methodCallsQueue.offer(methodCall);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public MethodCall getMethodCall() {
            return methodCallsQueue.remove();
        }

    }

    @Test
    public void testSimpleApp1() throws Exception {

        SerializableTcpServer tcpServer = new SerializableTcpServer();
        Thread t = new Thread(tcpServer);
        t.start();

        String[] commands = new String[]{
                "java",
                "-DanalyzerOutput=serializableTcpClient",
                "-javaagent:../../deploy/jTracert.jar",
                "-jar","deploy/simpleApp1.jar"
        };

        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.redirectErrorStream(true);
        processBuilder.directory(new File(".").getAbsoluteFile());

        Process process = processBuilder.start();

        int exitCode = process.waitFor();

        assertEquals(0, exitCode);

        MethodCall methodCall = tcpServer.getMethodCall();

        assertNotNull(methodCall);

    }

}

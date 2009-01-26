package com.google.code.jtracert.samples;

import com.google.code.jtracert.model.MethodCall;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class JTracertSerializableTcpServer implements Runnable {

    private Queue<MethodCall> methodCallsQueue = new ArrayBlockingQueue<MethodCall>(1);

    private int port;
    private Thread serverThread;

    public JTracertSerializableTcpServer(int port) {
        this();
        setPort(port);
    }

    public JTracertSerializableTcpServer() {

    }

    public Thread start() {
        serverThread = new Thread(this);
        serverThread.start();
        return serverThread;
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(getPort());
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

    public Thread getServerThread() {
        return serverThread;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public MethodCall getMethodCall() {
        return methodCallsQueue.remove();
    }

}
package com.google.code.jtracert.samples;

import com.google.code.jtracert.model.MethodCall;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class JTracertSerializableTcpServer implements Runnable {

    private Queue<MethodCall> methodCallsQueue = new LinkedBlockingQueue<MethodCall>();

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

            while (true) {

                System.out.println("Waiting for a connection");

                Socket socket = serverSocket.accept();

                System.out.println("Client connected");

                InputStream inputStream = socket.getInputStream();

                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                Object object = objectInputStream.readObject();

                System.out.println("Method call recieved");

                MethodCall methodCall = (MethodCall) object;

                methodCallsQueue.offer(methodCall);

                System.out.println("Method call is put to queue");
            }

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
        System.out.println("MethodCall was polled from the queue");
        return methodCallsQueue.poll();
    }

}
package com.google.code.jtracert.traceBuilder.impl.serializable;

import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.traceBuilder.impl.BaseMethodCallProcessor;
import com.google.code.jtracert.config.InstrumentationProperties;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.ServerSocket;
import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SerializableTcpServer extends BaseMethodCallProcessor implements Runnable {

    private int port;
    private volatile boolean running;
    public volatile boolean connected;

    private InstrumentationProperties instrumentationProperties;

    private BlockingQueue<MethodCall> methodCallQueue = new ArrayBlockingQueue<MethodCall>(5);
    private static SerializableTcpServer instance;

    private SerializableTcpServer(int port) {
        this.port = port;

        Thread serverThread = new Thread(this);
        serverThread.start();

    }

    public static SerializableTcpServer getIstance(int port) {
        if (null == instance) {
            instance = new SerializableTcpServer(port);
        }
        return instance;
    }

    public static SerializableTcpServer getIstance() {
        return instance;
    }

    public static void stop() {
        getIstance().running = false;
    }

    public void run() {
        running = true;
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            Socket socket;
            synchronized (this) {
                socket = serverSocket.accept();

                InputStream inputStream = socket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

                InstrumentationProperties instrumentationProperties =
                        (InstrumentationProperties) objectInputStream.readObject();

                setInstrumentationProperties(instrumentationProperties);

                connected = true;

                notifyAll();
            }

            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            while (running) {

                MethodCall methodCall = methodCallQueue.take();

                System.out.println("sending method call " + methodCall);


                objectOutputStream.writeObject(methodCall);

                objectOutputStream.flush();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processMethodCall(MethodCall methodCall) {
        try {
            System.out.println("putting method call " + methodCall);
            methodCallQueue.put(methodCall);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public InstrumentationProperties getInstrumentationProperties() {
        return instrumentationProperties;
    }

    public void setInstrumentationProperties(InstrumentationProperties instrumentationProperties) {
        this.instrumentationProperties = instrumentationProperties;
    }

}
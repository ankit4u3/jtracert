package com.google.code.jtracert.traceBuilder.impl.serializable;

import com.google.code.jtracert.config.InstrumentationProperties;
import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.traceBuilder.impl.BaseMethodCallProcessor;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 * @author Dmitry.Bedrin@gmail.com
 */
public class SerializableTcpServer extends BaseMethodCallProcessor implements Runnable {

    private int port;
    private volatile boolean running;
    public volatile boolean connected;

    private InstrumentationProperties instrumentationProperties;

    private BlockingQueue<MethodCall> methodCallQueue = new ArrayBlockingQueue<MethodCall>(5);
    private static SerializableTcpServer instance;

    /**
     *
     * @param port
     */
    private SerializableTcpServer(int port) {
        this.port = port;

        Thread serverThread = new Thread(this);
        serverThread.start();

    }

    /**
     *
     * @param port
     * @return
     */
    public static SerializableTcpServer getIstance(int port) {
        if (null == instance) {
            instance = new SerializableTcpServer(port);
        }
        return instance;
    }

    /**
     *
     * @return
     */
    public static SerializableTcpServer getIstance() {
        return instance;
    }

    /**
     *
     */
    public static void stop() {
        getIstance().running = false;
    }

    /**
     *
     */
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

                if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
                    System.out.println("sending method call " + methodCall);
                }

                objectOutputStream.writeObject(methodCall);

                objectOutputStream.flush();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param methodCall
     */
    public void processMethodCall(MethodCall methodCall) {
        try {
            if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
                System.out.println("putting method call " + methodCall);
            }
            methodCallQueue.put(methodCall);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public InstrumentationProperties getInstrumentationProperties() {
        return instrumentationProperties;
    }

    /**
     *
     * @param instrumentationProperties
     */
    public void setInstrumentationProperties(InstrumentationProperties instrumentationProperties) {
        this.instrumentationProperties = instrumentationProperties;
    }

}
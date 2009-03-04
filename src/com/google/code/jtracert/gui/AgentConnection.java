package com.google.code.jtracert.gui;

import com.google.code.jtracert.config.InstrumentationProperties;
import com.google.code.jtracert.model.MethodCall;

import javax.swing.event.EventListenerList;
import java.io.*;
import java.net.Socket;

public class AgentConnection implements Runnable {

    private AgentConnectionSettings settings;
    private Socket socket;
    private Thread agentClientThread;
    private volatile boolean running = false;

    private EventListenerList listeners = new EventListenerList();

    public AgentConnection(AgentConnectionSettings settings) {
        this.settings = settings;
        agentClientThread = new Thread(this);
    }

    public void connect() throws IOException {
        socket = new Socket(settings.getInetAddress(), settings.getPort());
    }

    public void start() {
        running = true;
        agentClientThread.start();
    }

    public void stop() {
        running = false;
    }

    public void run() {
        try {

            OutputStream socketOutputStream = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(socketOutputStream);

            InstrumentationProperties instrumentationProperties =
                    new InstrumentationProperties();

            instrumentationProperties.setClassNameRegEx(settings.getClassNamePattern());

            oos.writeObject(instrumentationProperties);

            oos.flush();

            InputStream socketInputStream = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(socketInputStream);

            while (running) {

                Object o = ois.readObject();
                methodCallRecieved((MethodCall) o );

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void methodCallRecieved(MethodCall methodCall) {
        MethodCallEvent methodCallEvent = new MethodCallEvent(methodCall);
        for (MethodCallListener methodCallListener : listeners.getListeners(MethodCallListener.class)) {
            methodCallListener.onMethodCall(methodCallEvent);
        }
    }

    public void addMethodCallListener(MethodCallListener methodCallListener) {
        listeners.add(MethodCallListener.class, methodCallListener);
    }

    public void removeMethodCallListener(MethodCallListener methodCallListener) {
        listeners.remove(MethodCallListener.class, methodCallListener);
    }


}

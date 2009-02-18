package com.google.code.jtracert.gui;

import java.net.Socket;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class AgentConnection {

    private AgentConnectionSettings settings;

    public AgentConnection(AgentConnectionSettings settings) {
        this.settings = settings;
    }

    public void connect() {

        try {
            Socket socket = new Socket(settings.getInetAddress(), settings.getPort());

            InputStream socketInputStream = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(socketInputStream);
            while (true) {

                Object o = ois.readObject();
                System.out.println(o);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

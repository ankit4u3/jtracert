package com.google.code.jtracert.traceBuilder.impl.serializable;

import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.traceBuilder.impl.BaseMethodCallProcessor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class SerializableTcpClient extends BaseMethodCallProcessor {

    /**
     * @param methodCall
     */
    public void processMethodCall(MethodCall methodCall) {

        String host = getAnalyzeProperties().getSerializableTcpServerHost();
        int port = getAnalyzeProperties().getSerializableTcpServerPort();

        Socket socket = null;
        OutputStream socketOutputStream = null;

        try {

            socket = new Socket(host, port);

            socketOutputStream = socket.getOutputStream();

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socketOutputStream);
            objectOutputStream.writeObject(methodCall);

            objectOutputStream.flush();
            objectOutputStream.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != socketOutputStream) {
                try {
                    socketOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != socket) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}

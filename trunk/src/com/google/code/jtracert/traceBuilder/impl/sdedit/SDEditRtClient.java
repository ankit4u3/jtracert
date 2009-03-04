package com.google.code.jtracert.traceBuilder.impl.sdedit;

import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.util.FileUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 * @author Dmitry.Bedrin@gmail.com
 */
public class SDEditRtClient extends BaseSDEditClient {

    /**
     *
     * @param methodCall
     */
    @Override
    public void processMethodCall(MethodCall methodCall) {

        String host = getAnalyzeProperties().getSdEditHost();
        int port = getAnalyzeProperties().getSdEditPort();

        Socket socket = null;
        OutputStream sdEditOutputStream = null;

        try {

            socket = new Socket(host, port);

            sdEditOutputStream = socket.getOutputStream();

            Writer diagramWriter = new OutputStreamWriter(sdEditOutputStream);

            diagramWriter.append("diagram name").append(FileUtils.LINE_SEPARATOR);
            diagramWriter.append("user:Actor").append(FileUtils.LINE_SEPARATOR);

            writeObjectNames(methodCall, diagramWriter);

            diagramWriter.append(FileUtils.LINE_SEPARATOR);

            diagramWriter.
                    append("user:").
                    append(methodCall.getClassName().replaceAll("\\.","\\\\.")).
                    append(".").
                    append(methodCall.getMethodName()).
                    append(FileUtils.LINE_SEPARATOR);

            writeMethodNames(methodCall, diagramWriter);

            diagramWriter.append("end");

            diagramWriter.flush();
            diagramWriter.close();


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != sdEditOutputStream) {
                try {
                    sdEditOutputStream.close();
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

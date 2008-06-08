package com.google.code.jtracert.traceBuilder.impl.sdedit;

import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.config.InstrumentationProperties;

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.io.OutputStreamWriter;

/**
 * @author dmitry.bedrin
 */
public class SDEditRtClient extends BaseSDEditClient {

    @Override
    public void processMethodCall(MethodCall methodCall) {

        String host = System.getProperty(InstrumentationProperties.HOST);
        int port = Integer.parseInt(System.getProperty(InstrumentationProperties.PORT));

        Socket socket = null;
        OutputStream sdEditOutputStream = null;

        try {

            socket = new Socket(host, port);

            sdEditOutputStream = socket.getOutputStream();

            Writer diagramWriter = new OutputStreamWriter(sdEditOutputStream);

            diagramWriter.append("diagram name").append(lineSeparator);
            diagramWriter.append("user:Actor").append(lineSeparator);

            writeObjectNames(methodCall, diagramWriter);

            diagramWriter.append(lineSeparator);

            diagramWriter.
                    append("user:").
                    append(methodCall.getRealClassName().replaceAll("\\.","\\\\.")).
                    append(".").
                    append(methodCall.getMethodName()).
                    append(lineSeparator);

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

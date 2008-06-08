package com.google.code.jtracert.traceBuilder.impl.sdedit;

import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.config.InstrumentationProperties;

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.util.Set;
import java.util.HashSet;

/**
 * @author dmitry.bedrin
 */
public class SDEditRtClient {

    public final static String newline = System.getProperty("line.separator");

    private Set<Integer> addedClassNames = new HashSet<Integer>();

    public void processMethodCall(MethodCall methodCall) {

        String host = System.getProperty(InstrumentationProperties.HOST);
        int port = Integer.parseInt(System.getProperty(InstrumentationProperties.PORT));

        Socket socket = null;
        OutputStream sdEditOutputStream = null;

        try {

            socket = new Socket(host, port);

            sdEditOutputStream = socket.getOutputStream();

            Writer diagramWriter = new OutputStreamWriter(sdEditOutputStream);

            diagramWriter.append("diagram name").append(newline);
            diagramWriter.append("user:Actor").append(newline);

            writeObjectNames(methodCall, diagramWriter);

            diagramWriter.append(newline);

            diagramWriter.
                    append("user:").
                    append(methodCall.getRealClassName().replaceAll("\\.","\\\\.")).
                    append(".").
                    append(methodCall.getMethodName()).
                    append(newline);

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

    private void writeObjectNames(MethodCall methodCall, Writer diagramWriter) throws IOException {

        String className = methodCall.getRealClassName();

        int classNameHash = className.hashCode();

        if (!addedClassNames.contains(classNameHash)) {
            addedClassNames.add(classNameHash);
            diagramWriter.
                    append(className).
                    append(':').
                    append(className).
                    append(newline);
        }

        for (MethodCall callee : methodCall.getCallees()) {
            writeObjectNames(callee, diagramWriter);
        }

    }

    private void writeMethodNames(MethodCall methodCall, Writer diagramWriter) throws IOException {

        int level = 0;

        for (MethodCall callee : methodCall.getCallees()) {

            diagramWriter.
                    append(methodCall.getRealClassName()).
                    append('[').
                    append(Integer.toString(level)).
                    append(']').
                    append(':').
                    append(callee.getRealClassName().replaceAll("\\.","\\\\.")).
                    append('.').
                    append(callee.getMethodName()).
                    append(newline);

            writeMethodNames(callee, diagramWriter);

        }

    }

}

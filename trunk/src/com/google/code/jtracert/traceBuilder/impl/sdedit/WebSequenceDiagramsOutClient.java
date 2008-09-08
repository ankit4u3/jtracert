package com.google.code.jtracert.traceBuilder.impl.sdedit;

import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.util.FileUtils;

import java.io.*;

/**
 * @author Dmitry Bedrin
 */
public class WebSequenceDiagramsOutClient extends BaseMethodCallProcessor {

    public void processMethodCall(MethodCall methodCall) {

        try {

            Writer diagramWriter = new OutputStreamWriter(System.out);

            writeSequence(methodCall, diagramWriter);

            diagramWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeSequence(MethodCall methodCall, Writer diagramWriter) throws IOException {

        for (MethodCall callee: methodCall.getCallees()) {
            
            diagramWriter.write(methodCall.getRealClassName());
            diagramWriter.write("->");
            diagramWriter.write(callee.getRealClassName());
            diagramWriter.write(":");
            diagramWriter.write(callee.getMethodName());
            diagramWriter.write(FileUtils.LINE_SEPARATOR);
            
            writeSequence(callee, diagramWriter);
        }

    }

}
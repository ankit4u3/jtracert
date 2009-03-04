package com.google.code.jtracert.traceBuilder.impl.webSequenceDiagrams;

import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.traceBuilder.impl.BaseMethodCallProcessor;
import com.google.code.jtracert.util.FileUtils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 * @author Dmitry.Bedrin@gmail.com
 */
public class WebSequenceDiagramsOutClient extends BaseMethodCallProcessor {

    /**
     *
     * @param methodCall
     */
    public void processMethodCall(MethodCall methodCall) {

        try {

            Writer diagramWriter = new OutputStreamWriter(System.out);

            writeSequence(methodCall, diagramWriter);

            diagramWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 
     * @param methodCall
     * @param diagramWriter
     * @throws IOException
     */
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
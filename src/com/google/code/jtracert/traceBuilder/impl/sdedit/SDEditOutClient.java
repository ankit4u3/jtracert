package com.google.code.jtracert.traceBuilder.impl.sdedit;

import com.google.code.jtracert.model.MethodCall;

import java.io.IOException;
import java.io.Writer;
import java.io.OutputStreamWriter;

/**
 * @author dmitry.bedrin
 */
public class SDEditOutClient extends BaseSDEditClient {

    @Override
    public void processMethodCall(MethodCall methodCall) {

        try {

            Writer diagramWriter = new OutputStreamWriter(System.out);

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

            diagramWriter.flush();
            diagramWriter.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
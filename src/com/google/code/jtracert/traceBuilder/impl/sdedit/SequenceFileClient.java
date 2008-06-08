package com.google.code.jtracert.traceBuilder.impl.sdedit;

import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.util.FileUtils;

import java.io.*;

/**
 * @author dmitry.bedrin
 */
public class SequenceFileClient {

    protected final static String lineSeparator = System.getProperty("line.separator");
    protected final static String fileSeparator = System.getProperty("file.separator");

    public void processMethodCall(MethodCall methodCall) {

        Writer diagramWriter = null;

        try {

            File diagramFile = getDiagramFile(methodCall);

            diagramWriter = new FileWriter(diagramFile);

            writeSequence(methodCall, diagramWriter, 0);

            diagramWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != diagramWriter) {
                try {
                    diagramWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void writeSequence(MethodCall methodCall, Writer diagramWriter, int level) throws IOException {

        String className = methodCall.getRealClassName().replaceAll("\\.","/");
        String methodName = methodCall.getMethodName().replaceAll("\\<","").replaceAll("\\>","");

        StringBuffer tabStringBuffer = new StringBuffer();
        for (int i = 0; i < level; i++) {
            tabStringBuffer.append("    ");
        }

        String tabString = tabStringBuffer.toString();

        diagramWriter.write(tabString);
        diagramWriter.write(className);
        diagramWriter.write('.');
        diagramWriter.write(methodName);
        diagramWriter.write(" {");
        diagramWriter.write(lineSeparator);

        for (MethodCall callee: methodCall.getCallees()) {
            writeSequence(callee, diagramWriter, level + 1);
        }

        diagramWriter.write("}");

    }

    private File getDiagramFile(MethodCall methodCall) throws IOException {

        String baseDiagramsFolderName = "C:/sequence";
        String fullClassName = methodCall.getRealClassName();

        StringBuffer diagramFolderNameStringBuffer = new StringBuffer(baseDiagramsFolderName);
        String[] classNameParts = fullClassName.split("\\.");
        for (int i = 0; i < classNameParts.length - 1; i++) {
            diagramFolderNameStringBuffer.append(fileSeparator).append(classNameParts[i]);
        }

        File diagramFolder = new File(diagramFolderNameStringBuffer.toString());

        FileUtils.forceMkdir(diagramFolder);

        diagramFolderNameStringBuffer.
                append(fileSeparator).
                append(classNameParts[classNameParts.length - 1]).
                append('.').
                append(methodCall.getMethodName());

        File diagramFile = new File(diagramFolderNameStringBuffer.toString() + ".sq");

        int i = 0;
        while (diagramFile.exists()) {
            i++;
            diagramFile = new File(diagramFolderNameStringBuffer.toString() + '.' + i + ".sq");
        }

        return diagramFile;

    }

}
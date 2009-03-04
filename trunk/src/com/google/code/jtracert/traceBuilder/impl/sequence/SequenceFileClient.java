package com.google.code.jtracert.traceBuilder.impl.sequence;

import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.traceBuilder.impl.BaseMethodCallProcessor;
import com.google.code.jtracert.util.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class SequenceFileClient extends BaseMethodCallProcessor {

    /**
     * @param methodCall
     */
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

    /**
     * @param methodCall
     * @param diagramWriter
     * @param level
     * @throws IOException
     */
    private void writeSequence(MethodCall methodCall, Writer diagramWriter, int level) throws IOException {

        String className = methodCall.getRealClassName().replaceAll("\\.", "/");
        String methodName = methodCall.getMethodName().replaceAll("\\<", "").replaceAll("\\>", ""); // todo refactor

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
        diagramWriter.write(FileUtils.LINE_SEPARATOR);

        for (MethodCall callee : methodCall.getCallees()) {
            writeSequence(callee, diagramWriter, level + 1);
        }

        diagramWriter.write(tabString);
        diagramWriter.write("}");
        diagramWriter.write(FileUtils.LINE_SEPARATOR);

    }

    /**
     * @param methodCall
     * @return
     * @throws IOException
     */
    private File getDiagramFile(MethodCall methodCall) throws IOException {

        String baseDiagramsFolderName = getAnalyzeProperties().getOutputFolder();
        String fullClassName = methodCall.getRealClassName();

        StringBuffer diagramFolderNameStringBuffer = new StringBuffer(baseDiagramsFolderName);
        String[] classNameParts = fullClassName.split("\\.");
        for (int i = 0; i < classNameParts.length - 1; i++) {
            diagramFolderNameStringBuffer.append(FileUtils.FILE_SEPARATOR).append(classNameParts[i]);
        }

        File diagramFolder = new File(diagramFolderNameStringBuffer.toString());

        FileUtils.forceMkdir(diagramFolder);

        diagramFolderNameStringBuffer.
                append(FileUtils.FILE_SEPARATOR).
                append(classNameParts[classNameParts.length - 1]).
                append('.').
                append(methodCall.getMethodName().replaceAll("\\<", "").replaceAll("\\>", "")); // todo refactor

        File diagramFile = new File(diagramFolderNameStringBuffer.toString() + ".sq");

        int i = 0;
        while (diagramFile.exists()) {
            i++;
            diagramFile = new File(diagramFolderNameStringBuffer.toString() + '.' + i + ".sq");
        }

        return diagramFile;

    }

}
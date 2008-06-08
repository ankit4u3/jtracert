package com.google.code.jtracert.traceBuilder.impl.sdedit;

import com.google.code.jtracert.model.MethodCall;

import java.io.Writer;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/**
 * @author dmitry.bedrin
 */
public abstract class BaseSDEditClient implements SDEditClient {

    protected final static String lineSeparator = System.getProperty("line.separator");
    protected final static String fileSeparator = System.getProperty("file.separator");

    private Set<Integer> addedClassNames = new HashSet<Integer>();
    private Map<String,Integer> classLevelMap = new HashMap<String,Integer>();

    public abstract void processMethodCall(MethodCall methodCall);

    protected void writeObjectNames(MethodCall methodCall, Writer diagramWriter) throws IOException {

        String className = methodCall.getRealClassName();

        int classNameHash = className.hashCode();

        if (!addedClassNames.contains(classNameHash)) {
            addedClassNames.add(classNameHash);
            diagramWriter.
                    append(className).
                    append(':').
                    append(className).
                    append(lineSeparator);
        }

        for (MethodCall callee : methodCall.getCallees()) {
            writeObjectNames(callee, diagramWriter);
        }

    }

    protected void writeMethodNames(MethodCall methodCall, Writer diagramWriter) throws IOException {

        String callerClassName = methodCall.getRealClassName();

        classLevelMap.put(callerClassName,0);

        for (MethodCall callee : methodCall.getCallees()) {

            String calleeClassName = callee.getRealClassName();

            int level = classLevelMap.get(callerClassName);

            diagramWriter.
                    append(callerClassName).
                    append('[').
                    append(Integer.toString(level)).
                    append(']').
                    append(':').
                    append(calleeClassName.replaceAll("\\.","\\\\.")).
                    append('.').
                    append(callee.getMethodName()).
                    append(lineSeparator);

            classLevelMap.put(callerClassName,0);

            writeMethodNames(callee, diagramWriter);

            if (callerClassName.equals(calleeClassName)) {
                classLevelMap.put(callerClassName,1 + classLevelMap.get(callerClassName));
            }

        }

    }

}
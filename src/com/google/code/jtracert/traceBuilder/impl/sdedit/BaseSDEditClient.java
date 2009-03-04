package com.google.code.jtracert.traceBuilder.impl.sdedit;

import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.traceBuilder.impl.BaseMethodCallProcessor;
import com.google.code.jtracert.util.FileUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author dmitry.bedrin
 */
public abstract class BaseSDEditClient extends BaseMethodCallProcessor {

    private final Set<Integer> addedClassNames = new HashSet<Integer>();
    private final Map<String,Integer> classLevelMap = new HashMap<String,Integer>();

    public abstract void processMethodCall(MethodCall methodCall);

    protected void writeObjectNames(MethodCall methodCall, Writer diagramWriter) throws IOException {

        String className = methodCall.getClassName();

        int classNameHash = className.hashCode();

        if (!addedClassNames.contains(classNameHash)) {
            addedClassNames.add(classNameHash);
            diagramWriter.
                    append(className).
                    append(':').
                    append(className).
                    append(FileUtils.LINE_SEPARATOR);
        }

        for (MethodCall callee : methodCall.getCallees()) {
            writeObjectNames(callee, diagramWriter);
        }

    }

    protected void writeMethodNames(MethodCall methodCall, Writer diagramWriter) throws IOException {

        String callerClassName = methodCall.getClassName();

        classLevelMap.put(callerClassName,0);

        for (MethodCall callee : methodCall.getCallees()) {

            String calleeClassName = callee.getClassName();

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
                    append(FileUtils.LINE_SEPARATOR);

            classLevelMap.put(callerClassName,0);

            writeMethodNames(callee, diagramWriter);

            if (callerClassName.equals(calleeClassName)) {
                classLevelMap.put(callerClassName,1 + classLevelMap.get(callerClassName));
            }

        }

    }

}
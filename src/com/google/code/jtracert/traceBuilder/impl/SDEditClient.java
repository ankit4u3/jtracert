package com.google.code.jtracert.traceBuilder.impl;

import com.google.code.jtracert.model.JTracertObjectCompanion;
import com.google.code.jtracert.model.MethodCall;

import java.util.*;

/**
 * @author Dmitry Bedrin
 */
public class SDEditClient {

    private Map<Object, String> objectNames = new HashMap<Object, String>();
    private Map<String, Integer> objectCount = new HashMap<String, Integer>();
    private Map<MethodCall, String> methodCallObjectNames = new HashMap<MethodCall, String>();

    public static String newline = System.getProperty("line.separator");
    private static Set<Integer> processedMethodCallGraphHashCodes = new HashSet<Integer>();

    public void processMethodCall(MethodCall methodCall) {

        // Analyzing call graph

        Set<String> headers = new LinkedHashSet<String>();

        headers = getObjectNames(methodCall, headers);

        Set<String> methodCallStrings = new LinkedHashSet<String>();

        methodCallStrings = getMethodCallStrings(methodCall, methodCallStrings);

        int methodCallGraphHashCode = methodCallStrings.hashCode();

        synchronized (processedMethodCallGraphHashCodes) {
        
            if (processedMethodCallGraphHashCodes.contains(methodCallGraphHashCode)) {
                return;
            } else {
                processedMethodCallGraphHashCodes.add(methodCallGraphHashCode);
            }

        }

        // Begining output

        StringBuffer diagrammStringBuffer = new StringBuffer();

        diagrammStringBuffer.append("user:Actor").append(newline);

        for (String header : headers) {
            diagrammStringBuffer.append(header).append(newline);
        }

        diagrammStringBuffer.append(newline);

        diagrammStringBuffer.append("user:" + methodCallObjectNames.get(methodCall) + "." + methodCall.getMethodName()).append(newline);

        for (String methodCallString : methodCallStrings) {
            diagrammStringBuffer.append(methodCallString).append(newline);
        }

        System.out.println(diagrammStringBuffer);


    }

    private Set<String> getMethodCallStrings(MethodCall methodCall, Set<String> methodCallStrings) {

        String callerName = methodCallObjectNames.get(methodCall);
        for (MethodCall callee : methodCall.getCallees()) {
            String calleeName = methodCallObjectNames.get(callee);

            methodCallStrings.add(callerName + ":" + calleeName + "." + callee.getMethodName());

            methodCallStrings.addAll(getMethodCallStrings(callee, methodCallStrings));

        }

        return methodCallStrings;
    }

    private Set<String> getObjectNames(MethodCall methodCall, Set<String> objectNames) {

        try {

            String className = methodCall.getRealClassName();

            String objectName;

            JTracertObjectCompanion jTracertObjectCompanion = methodCall.getjTracertObjectCompanion();

            if (null == jTracertObjectCompanion) {
                objectName = className;
            } else {

                if (!this.objectNames.containsKey(jTracertObjectCompanion)) {
                    this.objectNames.put(jTracertObjectCompanion, objectName = generateObjectName(className));
                } else {
                    objectName = this.objectNames.get(jTracertObjectCompanion);
                }
            }

            StringBuffer headerStringBuffer = new StringBuffer();
            headerStringBuffer.append(objectName.replaceAll("\\.","\\."));
            methodCallObjectNames.put(methodCall, objectName.replaceAll("\\.","\\\\."));
            headerStringBuffer.append(':');
            headerStringBuffer.append(className.replaceAll("\\.","\\."));

            if (null == jTracertObjectCompanion) {
                headerStringBuffer.append("[a]");
            }

            objectNames.add(headerStringBuffer.toString());

            for (MethodCall callee : methodCall.getCallees()) {
                objectNames.addAll(getObjectNames(callee, objectNames));
            }

            return objectNames;

        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    private String generateObjectName(String className) {

        int currentCount;

        if (objectCount.containsKey(className)) {
            currentCount = objectCount.get(className) + 1;
        } else {
            currentCount = 1;
        }

        objectCount.put(className, currentCount);

        return className + currentCount;

    }

}

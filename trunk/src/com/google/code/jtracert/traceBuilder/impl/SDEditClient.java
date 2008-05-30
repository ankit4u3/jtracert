package com.google.code.jtracert.traceBuilder.impl;

import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.model.JTracertObjectCompanion;

import java.util.*;

/**
 * @author Dmitry Bedrin
 */
public class SDEditClient {

    private Map<Object, String> objectNames = new HashMap<Object, String>();
    private Map<String, Integer> objectCount = new HashMap<String, Integer>();
    private Map<MethodCall, String> methodCallObjectNames = new HashMap<MethodCall, String>();

    public void processMethodCall(MethodCall methodCall) {

        System.out.println("user:Actor");

        Set<String> headers = new LinkedHashSet<String>();

        headers = getObjectNames(methodCall, headers);

        for (String header : headers) {
            System.out.println(header);
        }

        headers = null;

        System.out.println();

        Set<String> methodCallStrings = new LinkedHashSet<String>();

        methodCallStrings = getMethodCallStrings(methodCall, methodCallStrings);

        System.out.println("user:" + methodCallObjectNames.get(methodCall) + "." + methodCall.getMethodName());

        for (String methodCallString : methodCallStrings) {
            System.out.println(methodCallString);
        }


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

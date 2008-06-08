package com.google.code.jtracert.traceBuilder.impl;

import com.google.code.jtracert.model.JTracertObjectCompanion;
import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.config.InstrumentationProperties;

import java.util.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.Writer;
import java.io.OutputStreamWriter;

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

        List<String> methodCallStrings = new LinkedList<String>();

        methodCallStrings = getMethodCallStrings(methodCall, methodCallStrings);

        // Begining output

        String agentType = System.getProperty(InstrumentationProperties.TYPE);

        if ((null != agentType) && (agentType.equals("client"))) {
            String host = System.getProperty(InstrumentationProperties.HOST);
            int port = Integer.parseInt(System.getProperty(InstrumentationProperties.PORT));
            sendDiagramToRTServer(methodCall, headers, methodCallStrings, host, port);
        } else {
            printDiagram(methodCall, headers, methodCallStrings);
            //System.out.println("!!!!!!!!!!!! DIAGRAM COMPLETE");
        }



    }

    private void printDiagram(MethodCall methodCall, Set<String> headers, Collection<String> methodCallStrings) {
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

    private void sendDiagramToRTServer(MethodCall methodCall, Set<String> headers, Collection<String> methodCallStrings, String host, int port) {
        Socket socket = null;

        Writer diagramWriter = null;

        try {

            socket = new Socket(host, port);

            diagramWriter = new OutputStreamWriter(socket.getOutputStream());

            diagramWriter.append("diagram name").append(newline);

            diagramWriter.append("user:Actor").append(newline);

            for (String header : headers) {
                diagramWriter.append(header).append(newline);
            }

            diagramWriter.append(newline);

            diagramWriter.append("user:" + methodCallObjectNames.get(methodCall) + "." + methodCall.getMethodName()).append(newline);

            for (String methodCallString : methodCallStrings) {
                diagramWriter.append(methodCallString).append(newline);
            }

            diagramWriter.append("end");

            diagramWriter.flush();

        } catch (UnknownHostException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                socket.getOutputStream().close();
                diagramWriter.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private Map<String,Integer> objectReturnCountMap = new HashMap<String,Integer>();

    private List<String> getMethodCallStrings(MethodCall methodCall, List<String> methodCallStrings) {

        String callerName = methodCallObjectNames.get(methodCall);

        int objectReturnCount;

        if (!objectReturnCountMap.containsKey(callerName)) {
            objectReturnCountMap.put(callerName,0);
        }

        for (MethodCall callee : methodCall.getCallees()) {

            String calleeName = methodCallObjectNames.get(callee);

            objectReturnCount = objectReturnCountMap.get(callerName);

            methodCallStrings.add(callerName + "[" + objectReturnCount + "]:" + calleeName + "." + callee.getMethodName());

            objectReturnCountMap.put(callerName,0);

            if (!callerName.equals(calleeName)) {
                objectReturnCountMap.put(calleeName,0);
            }
            methodCallStrings.addAll(getMethodCallStrings(callee, methodCallStrings));

            if (callerName.equals(calleeName)) {
                objectReturnCount = objectReturnCountMap.get(callerName);
                objectReturnCount++;
                objectReturnCountMap.put(callerName,objectReturnCount);
            } else {
                objectReturnCountMap.put(calleeName,0);
            }

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

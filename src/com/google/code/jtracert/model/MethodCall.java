package com.google.code.jtracert.model;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.io.Serializable;

/**
 * @author Dmitry Bedrin
 */
public class MethodCall implements Serializable  {

    private String methodName;
    private String methodSignature;

    private JTracertObjectCompanion jTracertObjectCompanion;
    private String realClassName;

    private List<MethodCall> callees;
    private MethodCall calleer;

    public MethodCall() {
        this.setCallees(new LinkedList<MethodCall>());
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodSignature() {
        return methodSignature;
    }

    public void setMethodSignature(String methodSignature) {
        this.methodSignature = methodSignature;
    }

    public void addCallee(MethodCall methodCall) {
        methodCall.setCalleer(this);
        this.getCallees().add(methodCall);
    }

    public MethodCall getCalleer() {
        return calleer;
    }

    public void setCalleer(MethodCall calleer) {
        this.calleer = calleer;
    }

    public List<MethodCall> getCallees() {
        return callees;
    }

    public void setCallees(List<MethodCall> callees) {
        this.callees = callees;
    }

    public JTracertObjectCompanion getjTracertObjectCompanion() {
        return jTracertObjectCompanion;
    }

    public void setjTracertObjectCompanion(JTracertObjectCompanion jTracertObjectCompanion) {
        this.jTracertObjectCompanion = jTracertObjectCompanion;
    }

    public String getRealClassName() {
        return realClassName;
    }

    public void setRealClassName(String realClassName) {
        this.realClassName = realClassName;
    }

}

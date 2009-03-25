package com.google.code.jtracert.client.model;

import java.util.List;
import java.util.LinkedList;

public class MethodCall {

    private Class clazz;
    private Method method;

    private String resolvedClassName;
    private String resolvedMethodName;

    private List<MethodCall> callees;

    public MethodCall(String resolvedClassName, String resolvedMethodName) {
        this.resolvedClassName = resolvedClassName;
        this.resolvedMethodName = resolvedMethodName;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getResolvedClassName() {
        return resolvedClassName;
    }

    public void setResolvedClassName(String resolvedClassName) {
        this.resolvedClassName = resolvedClassName;
    }

    public String getResolvedMethodName() {
        return resolvedMethodName;
    }

    public void setResolvedMethodName(String resolvedMethodName) {
        this.resolvedMethodName = resolvedMethodName;
    }

    public List<MethodCall> getCallees() {
        return callees;
    }

    public void setCallees(List<MethodCall> callees) {
        this.callees = callees;
    }

    public void addCallee(MethodCall callee) {
        if (null == getCallees()) {
            setCallees(new LinkedList<MethodCall>());
        }
        getCallees().add(callee);
    }

}

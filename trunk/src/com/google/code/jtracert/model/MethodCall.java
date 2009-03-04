package com.google.code.jtracert.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 * @author Dmitry.Bedrin@gmail.com
 */
public class MethodCall implements Serializable  {

    static final long serialVersionUID = -1692513983094960602L;

    private String methodName;
    private String methodSignature;

    private JTracertObjectCompanion jTracertObjectCompanion;
    private String realClassName;
    private String className;

    private List<MethodCall> callees;
    private MethodCall calleer;

    private int callCount;
    private int objectHashCode;

    /**
     *
     */
    public MethodCall() {
        this.setCallees(new LinkedList<MethodCall>());
        this.callCount = 1;
    }

    /**
     *
     * @param className
     * @param methodName
     * @param methodSignature
     * @param jTracertObjectCompanion
     */
    public MethodCall(String className, String methodName, String methodSignature, JTracertObjectCompanion jTracertObjectCompanion) {
        this();
        this.realClassName = className;
        this.methodName = methodName;
        this.methodSignature = methodSignature;
        this.jTracertObjectCompanion = jTracertObjectCompanion;
    }

    /**
     *
     * @return
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     *
     * @param methodName
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     *
     * @return
     */
    public String getMethodSignature() {
        return methodSignature;
    }

    /**
     *
     * @param methodSignature
     */
    public void setMethodSignature(String methodSignature) {
        this.methodSignature = methodSignature;
    }

    /**
     *
     * @param methodCall
     */
    public void addCallee(MethodCall methodCall) {
        methodCall.setCalleer(this);
        this.getCallees().add(methodCall);
    }

    /**
     *
     * @return
     */
    public MethodCall getCalleer() {
        return calleer;
    }

    /**
     *
     * @param calleer
     */
    public void setCalleer(MethodCall calleer) {
        this.calleer = calleer;
    }

    /**
     *
     * @return
     */
    public List<MethodCall> getCallees() {
        return callees;
    }

    /**
     *
     * @param callees
     */
    public void setCallees(List<MethodCall> callees) {
        this.callees = callees;
    }

    /**
     *
     * @return
     */
    public JTracertObjectCompanion getjTracertObjectCompanion() {
        return jTracertObjectCompanion;
    }

    /**
     *
     * @param jTracertObjectCompanion
     */
    public void setjTracertObjectCompanion(JTracertObjectCompanion jTracertObjectCompanion) {
        this.jTracertObjectCompanion = jTracertObjectCompanion;
    }

    /**
     *
     * @return
     */
    public String getRealClassName() {
        return realClassName;
    }

    /**
     *
     * @param realClassName
     */
    public void setRealClassName(String realClassName) {
        this.realClassName = realClassName;
    }

    /**
     *
     * @return
     */
    public int getCallCount() {
        return callCount;
    }

    /**
     *
     * @param callCount
     */
    public void setCallCount(int callCount) {
        this.callCount = callCount;
    }

    /**
     *
     */
    public void incrementCallCount() {
        this.callCount++;
    }

    /**
     *
     * @return
     */
    public int getObjectHashCode() {
        return objectHashCode;
    }

    /**
     *
     * @param objectHashCode
     */
    public void setObjectHashCode(int objectHashCode) {
        this.objectHashCode = objectHashCode;
    }

    /**
     *
     * @return
     */
    public String getClassName() {
        return className;
    }

    /**
     *
     * @param className
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     *
     * @param visitor
     * @param <T>
     * @return
     */
    public <T> T accept(MethodCallVisitor<T> visitor) {
        return visitor.visit(this);
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MethodCall[");
        builder.append("className=").append(getRealClassName()).append(';');
        builder.append("methodName=").append(getMethodName()).append(';');
        builder.append("methodSignature=").append(getMethodSignature()).append(';');
        builder.append("callCount=").append(getCallCount()).append(']');
        return builder.toString();
    }
}

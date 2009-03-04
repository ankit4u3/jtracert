package com.google.code.jtracert.model;

import com.google.code.jtracert.model.MethodCallVisitor;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dmitry Bedrin
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

    public MethodCall() {
        this.setCallees(new LinkedList<MethodCall>());
        this.callCount = 1;
    }

    public MethodCall(String className, String methodName, String methodSignature, JTracertObjectCompanion jTracertObjectCompanion) {
        this();
        this.realClassName = className;
        this.methodName = methodName;
        this.methodSignature = methodSignature;
        this.jTracertObjectCompanion = jTracertObjectCompanion;
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

    public int getCallCount() {
        return callCount;
    }

    public void setCallCount(int callCount) {
        this.callCount = callCount;
    }

    public void incrementCallCount() {
        this.callCount++;
    }

    public int getObjectHashCode() {
        return objectHashCode;
    }

    public void setObjectHashCode(int objectHashCode) {
        this.objectHashCode = objectHashCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public <T> T accept(MethodCallVisitor<T> visitor) {
        return visitor.visit(this);
    }

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

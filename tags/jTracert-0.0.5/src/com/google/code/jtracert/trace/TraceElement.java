package com.google.code.jtracert.trace;

import java.util.LinkedList;
import java.util.List;

public abstract class TraceElement {

    private Class clazz;
    private Object object;

    private TraceElement parentTraceElement;
    private List<TraceElement> childTraceElements;

    protected TraceElement(Class clazz, Object object) {
        this();
        this.clazz = clazz;
        this.object = object;
    }

    private TraceElement() {
        childTraceElements = new LinkedList<TraceElement>();
    }

    public void addTraceElement(TraceElement traceElement) {
        traceElement.setParentTraceElement(this);
        childTraceElements.add(traceElement);
    }

    public abstract String getName();

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public TraceElement getParentTraceElement() {
        return parentTraceElement;
    }

    public void setParentTraceElement(TraceElement parentTraceElement) {
        this.parentTraceElement = parentTraceElement;
    }

    public List<TraceElement> getChildTraceElements() {
        return childTraceElements;
    }

    public void setChildTraceElements(List<TraceElement> childTraceElements) {
        this.childTraceElements = childTraceElements;
    }

}

package com.google.code.jtracert.trace;

public class TraceBuilderState {

    private TraceElement contextTraceElement;
    private Constructor constructor;
    public int enterCount;
    public int leaveCount;

    public TraceBuilderState(TraceElement contextTraceElement) {
        this.contextTraceElement = contextTraceElement;
    }

    public TraceElement getContextTraceElement() {
        return contextTraceElement;
    }

    public void setContextTraceElement(TraceElement contextTraceElement) {
        this.contextTraceElement = contextTraceElement;
    }

    public Constructor getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor constructor) {
        this.constructor = constructor;
    }

}

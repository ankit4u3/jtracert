package com.google.code.jtracert.traceBuilder.impl.sdedit;

import com.google.code.jtracert.traceBuilder.impl.MethodCallProcessor;
import com.google.code.jtracert.config.AnalyzeProperties;

/**
 * @author Dmitry Bedrin
 */
public abstract class BaseMethodCallProcessor implements MethodCallProcessor {

    private AnalyzeProperties analyzeProperties;

    public AnalyzeProperties getAnalyzeProperties() {
        return analyzeProperties;
    }

    public void setAnalyzeProperties(AnalyzeProperties analyzeProperties) {
        this.analyzeProperties = analyzeProperties;
    }

}

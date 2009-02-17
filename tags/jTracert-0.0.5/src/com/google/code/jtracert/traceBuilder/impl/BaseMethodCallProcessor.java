package com.google.code.jtracert.traceBuilder.impl;

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

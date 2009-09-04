package com.google.code.jtracert.traceBuilder.impl;

import com.google.code.jtracert.config.AnalyzeProperties;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public abstract class BaseMethodCallProcessor implements MethodCallProcessor {

    private AnalyzeProperties analyzeProperties;

    /**
     * @return
     */
    public AnalyzeProperties getAnalyzeProperties() {
        return analyzeProperties;
    }

    /**
     * @param analyzeProperties
     */
    public void setAnalyzeProperties(AnalyzeProperties analyzeProperties) {
        this.analyzeProperties = analyzeProperties;
    }

}

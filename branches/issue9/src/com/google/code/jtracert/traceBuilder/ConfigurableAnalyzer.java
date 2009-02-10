package com.google.code.jtracert.traceBuilder;

import com.google.code.jtracert.config.AnalyzeProperties;

/**
 * @author Dmitry Bedrin
 */
public interface ConfigurableAnalyzer {

    AnalyzeProperties getAnalyzeProperties();

    void setAnalyzeProperties(AnalyzeProperties analyzeProperties);

}

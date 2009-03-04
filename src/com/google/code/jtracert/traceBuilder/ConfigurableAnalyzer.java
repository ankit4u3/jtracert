package com.google.code.jtracert.traceBuilder;

import com.google.code.jtracert.config.AnalyzeProperties;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public interface ConfigurableAnalyzer {

    /**
     * @return
     */
    AnalyzeProperties getAnalyzeProperties();

    /**
     * @param analyzeProperties
     */
    void setAnalyzeProperties(AnalyzeProperties analyzeProperties);

}

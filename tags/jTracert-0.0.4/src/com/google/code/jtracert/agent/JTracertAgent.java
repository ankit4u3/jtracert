package com.google.code.jtracert.agent;

import com.google.code.jtracert.config.InstrumentationProperties;
import com.google.code.jtracert.config.AnalyzeProperties;
import com.google.code.jtracert.traceBuilder.MethodCallTraceBuilderFactory;

import java.lang.instrument.Instrumentation;

/**
 * @author Dmitry Bedrin
 */
public class JTracertAgent {

    public static void premain(final String arg, Instrumentation instrumentation) {

        InstrumentationProperties instrumentationProperties =
                InstrumentationProperties.loadFromSystemProperties();

        JTracertClassFileTransformer jTracertClassFileTransformer =
                new JTracertClassFileTransformer(instrumentationProperties);

        AnalyzeProperties analyzeProperties =
                AnalyzeProperties.loadFromSystemProperties();

        MethodCallTraceBuilderFactory.
                configureMethodCallTraceBuilder(analyzeProperties);
        
        instrumentation.
                addTransformer(jTracertClassFileTransformer);

    }

}

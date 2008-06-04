package com.google.code.jtracert.agent;

import com.google.code.jtracert.config.InstrumentationProperties;

import java.lang.instrument.Instrumentation;

/**
 * @author Dmitry Bedrin
 */
public class JTracertAgent {

    public static void premain(final String arg, Instrumentation instrumentation) {

        InstrumentationProperties instrumentationProperties = new InstrumentationProperties();

        JTracertClassFileTransformer jTracertClassFileTransformer = new JTracertClassFileTransformer(instrumentationProperties);
        
        instrumentation.addTransformer(jTracertClassFileTransformer);

    }

}

package com.google.code.jtracert.agent;

import java.lang.instrument.Instrumentation;

import com.google.code.jtracert.config.InstrumentationProperties;

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

package com.google.code.jtracert.agent;

import com.google.code.jtracert.config.AnalyzeProperties;
import com.google.code.jtracert.config.InstrumentationProperties;
import static com.google.code.jtracert.config.AnalyzeProperties.AnalyzerOutput.serializableTcpServer;
import com.google.code.jtracert.traceBuilder.MethodCallTraceBuilderFactory;
import com.google.code.jtracert.traceBuilder.impl.serializable.SerializableTcpServer;

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

        if ((null != arg) && (!"".equals(arg))) {

            try {
                int port = Integer.parseInt(arg);
                analyzeProperties.setAnalyzerOutput(serializableTcpServer);
                analyzeProperties.setSerializableTcpServerPort(port);
                SerializableTcpServer.getIstance(port);

                Runtime.getRuntime().addShutdownHook(new Thread(
                    new Runnable() {
                        public void run() {
                            SerializableTcpServer.stop();
                        }
                    }
                ));

            } catch (NumberFormatException e) {
                e.printStackTrace(System.err);
            }
        }

        MethodCallTraceBuilderFactory.
                configureMethodCallTraceBuilder(analyzeProperties);
        
        instrumentation.
                addTransformer(jTracertClassFileTransformer);

    }

}

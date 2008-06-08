package com.google.code.jtracert.agent;

import com.google.code.jtracert.ProjectInfo;
import com.google.code.jtracert.filter.ClassFilterProcessor;
import com.google.code.jtracert.filter.impl.AllowClassByNameRegExFilter;
import com.google.code.jtracert.config.InstrumentationProperties;
import com.google.code.jtracert.instrument.ByteCodeTransformException;
import com.google.code.jtracert.instrument.ConfigurableTransformer;
import com.google.code.jtracert.instrument.JTracertByteCodeTransformer;
import com.google.code.jtracert.instrument.JTracertByteCodeTransformerFactory;
import com.google.code.jtracert.instrument.impl.adapter.JTracertByteCodeTransformerAdapter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.io.FileOutputStream;

/**
 * @author Dmitry Bedrin
 */
public class JTracertClassFileTransformer implements ClassFileTransformer, ConfigurableTransformer {

    private InstrumentationProperties instrumentationProperties;

    public JTracertClassFileTransformer() {

    }

    public JTracertClassFileTransformer(InstrumentationProperties instrumentationProperties) {
        this();
        this.instrumentationProperties = instrumentationProperties;
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        className = className.replace('/','.');

        ClassFilterProcessor classFilterProcessor = new ClassFilterProcessor();

        String classNameRegEx = System.getProperty(InstrumentationProperties.CLASS_NAME_REGEX);

        if (null != classNameRegEx) {
            classFilterProcessor.addFilter(new AllowClassByNameRegExFilter(classNameRegEx));
        }

        if (!classFilterProcessor.processClass(className, loader)) {
            return null;
        }

        System.out.println("Transforming " + className);

        JTracertByteCodeTransformer jTracertByteCodeTransformer =
                JTracertByteCodeTransformerFactory.getJTracertByteCodeTransformer(getInstrumentationProperties());

        JTracertByteCodeTransformerAdapter jTracertByteCodeTransformerAdapter =
                new JTracertByteCodeTransformerAdapter(jTracertByteCodeTransformer);

        try {
            byte[] transformedData = jTracertByteCodeTransformerAdapter.transform(classfileBuffer);
            if (className.startsWith("net.tmobile.security.provider.authentication.base.weblogic.TMAuthenticationProvider")) {
                try {
                    FileOutputStream fos = new FileOutputStream("C:\\TMAuthenticationProvider.class");
                    fos.write(transformedData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return transformedData;
        } catch (ByteCodeTransformException e) {
            throw new IllegalClassFormatException(e.getMessage());
        }

    }

    public InstrumentationProperties getInstrumentationProperties() {
        return instrumentationProperties;
    }

    public void setInstrumentationProperties(InstrumentationProperties instrumentationProperties) {
        this.instrumentationProperties = instrumentationProperties;
    }

}

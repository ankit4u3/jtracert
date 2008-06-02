package com.google.code.jtracert.agent;

import com.google.code.jtracert.ProjectInfo;
import com.google.code.jtracert.config.InstrumentationProperties;
import com.google.code.jtracert.instrument.ByteCodeTransformException;
import com.google.code.jtracert.instrument.ConfigurableTransformer;
import com.google.code.jtracert.instrument.JTracertByteCodeTransformer;
import com.google.code.jtracert.instrument.JTracertByteCodeTransformerFactory;
import com.google.code.jtracert.instrument.impl.adapter.JTracertByteCodeTransformerAdapter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

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

        if (className.startsWith(ProjectInfo.PROJECT_PACKAGE_NAME)) return null;

        //if (loader != ClassLoader.getSystemClassLoader()) return null;

        boolean isSystemClassLoaderChild = false;

        do {
            if (loader == ClassLoader.getSystemClassLoader()) isSystemClassLoaderChild = true;
            loader = loader.getParent();
        } while (loader != null);

        if (!isSystemClassLoaderChild) return null;

        JTracertByteCodeTransformer jTracertByteCodeTransformer =
                JTracertByteCodeTransformerFactory.getJTracertByteCodeTransformer(getInstrumentationProperties());

        JTracertByteCodeTransformerAdapter jTracertByteCodeTransformerAdapter =
                new JTracertByteCodeTransformerAdapter(jTracertByteCodeTransformer);

        try {
            return jTracertByteCodeTransformerAdapter.transform(classfileBuffer);
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

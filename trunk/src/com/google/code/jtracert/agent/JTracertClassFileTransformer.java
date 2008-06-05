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

        if (className.startsWith(ProjectInfo.PROJECT_PACKAGE_NAME)) return null;

        /*if (className.startsWith("sun.reflect")) return null; // MAGIC

        if (className.startsWith("weblogic")) return null; // Early OutOfMemory
        if (className.startsWith("com.bea")) return null;

        if (className.startsWith("javax.servlet")) return null; // Invalid Length in LocalVariableTable
        if (className.startsWith("com.octetstring")) return null; // Invalid Length in LocalVariableTable*/

        /*boolean isSystemClassLoaderChild = false;

        do {
            if (loader == ClassLoader.getSystemClassLoader()) isSystemClassLoaderChild = true;
            loader = loader.getParent();
        } while (loader != null);

        if (!isSystemClassLoaderChild) return null;*/

        if (loader != ClassLoader.getSystemClassLoader()) return null;

        //if (!className.startsWith("net.tmobile")) return null;

//        System.out.println("Transforming " + className);

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

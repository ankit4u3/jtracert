package com.google.code.jtracert.agent;

import com.google.code.jtracert.classFilter.ClassFilterProcessor;
import com.google.code.jtracert.classFilter.ClassFilterable;
import com.google.code.jtracert.classFilter.impl.AllowClassByNameRegExFilter;
import com.google.code.jtracert.config.InstrumentationProperties;
import com.google.code.jtracert.instrument.ByteCodeTransformException;
import com.google.code.jtracert.instrument.ConfigurableTransformer;
import com.google.code.jtracert.instrument.JTracertByteCodeTransformer;
import com.google.code.jtracert.instrument.JTracertByteCodeTransformerFactory;
import com.google.code.jtracert.instrument.impl.adapter.JTracertByteCodeTransformerAdapter;
import com.google.code.jtracert.util.ClassUtils;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Dmitry Bedrin
 */
public class JTracertClassFileTransformer
        implements ClassFileTransformer, ConfigurableTransformer, ClassFilterable {

    private InstrumentationProperties instrumentationProperties;
    private ClassFilterProcessor classFilterProcessor;

    public JTracertClassFileTransformer() {
        this(null, new ClassFilterProcessor());
    }

    public JTracertClassFileTransformer(InstrumentationProperties instrumentationProperties) {
        this(instrumentationProperties, new ClassFilterProcessor());
    }

    public JTracertClassFileTransformer(ClassFilterProcessor classFilterProcessor) {
        this(null, classFilterProcessor);
    }

    public JTracertClassFileTransformer(InstrumentationProperties instrumentationProperties, ClassFilterProcessor classFilterProcessor) {
        super();
        this.instrumentationProperties = instrumentationProperties;
        this.classFilterProcessor = classFilterProcessor;
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        className = ClassUtils.getFullyQualifiedName(className);

        ClassFilterProcessor classFilterProcessor = new ClassFilterProcessor();

        String classNameRegEx = getInstrumentationProperties().getClassNameRegEx();

        if (null != classNameRegEx) {
            classFilterProcessor.addFilter(new AllowClassByNameRegExFilter(classNameRegEx));
        }

        if (!classFilterProcessor.processClass(className, loader)) {
            return null;
        }

        if (getInstrumentationProperties().isVerbose()) {
            System.out.println("Transforming " + className);
        }

        JTracertByteCodeTransformer jTracertByteCodeTransformer =
                JTracertByteCodeTransformerFactory.getJTracertByteCodeTransformer(getInstrumentationProperties());

        JTracertByteCodeTransformerAdapter jTracertByteCodeTransformerAdapter =
                new JTracertByteCodeTransformerAdapter(jTracertByteCodeTransformer);

        try {
            byte[] transformedData = jTracertByteCodeTransformerAdapter.transform(classfileBuffer);
            if (getInstrumentationProperties().isDumpTransformedClasses()) {
                dumpTransformedClass(className, transformedData);
            }
            return transformedData;
        } catch (ByteCodeTransformException e) {
            e.printStackTrace();
            throw new IllegalClassFormatException(e.getMessage());
        }

    }

    /**
     * @todo refactor this method in order to make path for classes configurable
     * @todo refactor this method and build folder structure when dumping classes
     * @param className
     * @param transformedData
     */
    private void dumpTransformedClass(String className, byte[] transformedData) {
        try {
            File debugFile = new File("/tmp/" + className + ".class");
            OutputStream outputStream = new FileOutputStream(debugFile);
            outputStream.write(transformedData);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public InstrumentationProperties getInstrumentationProperties() {
        return instrumentationProperties;
    }

    public void setInstrumentationProperties(InstrumentationProperties instrumentationProperties) {
        this.instrumentationProperties = instrumentationProperties;
    }

    public ClassFilterProcessor getClassFilterProcessor() {
        return classFilterProcessor;
    }

    public void setClassFilterProcessor(ClassFilterProcessor classFilterProcessor) {
        this.classFilterProcessor = classFilterProcessor;
    }

}
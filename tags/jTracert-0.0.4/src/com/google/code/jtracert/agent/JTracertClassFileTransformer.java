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

    public ClassFilterProcessor getClassFilterProcessor() {
        return classFilterProcessor;
    }

    public void setClassFilterProcessor(ClassFilterProcessor classFilterProcessor) {
        this.classFilterProcessor = classFilterProcessor;
    }

}
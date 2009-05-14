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

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class JTracertClassFileTransformer
        implements ClassFileTransformer, ConfigurableTransformer, ClassFilterable {

    private InstrumentationProperties instrumentationProperties;
    private ClassFilterProcessor classFilterProcessor;

    /**
     *
     */
    public JTracertClassFileTransformer() {
        this(null, new ClassFilterProcessor());
    }

    /**
     * @param instrumentationProperties
     */
    public JTracertClassFileTransformer(InstrumentationProperties instrumentationProperties) {
        this(instrumentationProperties, new ClassFilterProcessor());
    }

    /**
     * @param classFilterProcessor
     */
    public JTracertClassFileTransformer(ClassFilterProcessor classFilterProcessor) {
        this(null, classFilterProcessor);
    }

    /**
     * @param instrumentationProperties
     * @param classFilterProcessor
     */
    public JTracertClassFileTransformer(InstrumentationProperties instrumentationProperties, ClassFilterProcessor classFilterProcessor) {
        super();
        this.instrumentationProperties = instrumentationProperties;
        this.classFilterProcessor = classFilterProcessor;
    }

    /**
     * @param loader
     * @param className
     * @param classBeingRedefined
     * @param protectionDomain
     * @param classfileBuffer
     * @return
     * @throws IllegalClassFormatException
     */
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        try {

            if (className.equals("java/lang/Object")) {
                System.out.println("Instrumenting java.lang.Object");
            }

            if (getInstrumentationProperties().isVerbose()) {
                System.out.println("Checking filters for " + className);
            }

            className = ClassUtils.getFullyQualifiedName(className);

            ClassFilterProcessor classFilterProcessor = new ClassFilterProcessor();

            String classNameRegEx = getInstrumentationProperties().getClassNameRegEx();

            if (!classFilterProcessor.processClass(className, loader)) {
                return null;
            }

            if (null != classNameRegEx) {
                classFilterProcessor.addFilter(new AllowClassByNameRegExFilter(classNameRegEx));
            }

            boolean instrumentClass = classFilterProcessor.processClass(className, loader);

            if (getInstrumentationProperties().isVerbose()) {
                System.out.println("Transforming " + className);
            }

            JTracertByteCodeTransformer jTracertByteCodeTransformer =
                    JTracertByteCodeTransformerFactory.getJTracertByteCodeTransformer(getInstrumentationProperties());

            JTracertByteCodeTransformerAdapter jTracertByteCodeTransformerAdapter =
                    new JTracertByteCodeTransformerAdapter(jTracertByteCodeTransformer);

            try {
                byte[] transformedData = jTracertByteCodeTransformerAdapter.transform(classfileBuffer, instrumentClass);
                if (getInstrumentationProperties().isDumpTransformedClasses()) {
                    dumpTransformedClass(className, transformedData);
                }

                if (getInstrumentationProperties().isVerbose()) {
                    System.out.println("Transformation complete " + className);
                }

                return transformedData;
            } catch (ByteCodeTransformException e) {
                e.printStackTrace();
                throw new IllegalClassFormatException("Failed to transform bytecode of class " + className);
            }

        } catch (Throwable e) {
            e.printStackTrace();
            throw new IllegalClassFormatException(e.getMessage());
        }

    }

    /**
     * @param className       class name
     * @param transformedData bytecode of the class
     * @todo refactor this method in order to make path for classes configurable
     * @todo refactor this method and build folder structure when dumping classes
     */
    @Deprecated
    private void dumpTransformedClass(String className, byte[] transformedData) {
        try {

            String tmpDir = System.getProperty("java.io.tmpdir");
            String dumpDirPath = tmpDir + "/jTracertDumps";

            File dumpDir = new File(dumpDirPath);
            if (!dumpDir.exists()) {
                dumpDir.mkdir();
            }

            File debugFile = new File(dumpDirPath + "/" + className + ".class");
            OutputStream outputStream = new FileOutputStream(debugFile);
            outputStream.write(transformedData);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return
     */
    public InstrumentationProperties getInstrumentationProperties() {
        return instrumentationProperties;
    }

    /**
     * @param instrumentationProperties
     */
    public void setInstrumentationProperties(InstrumentationProperties instrumentationProperties) {
        this.instrumentationProperties = instrumentationProperties;
    }

    /**
     * @return
     */
    public ClassFilterProcessor getClassFilterProcessor() {
        return classFilterProcessor;
    }

    /**
     * @param classFilterProcessor
     */
    public void setClassFilterProcessor(ClassFilterProcessor classFilterProcessor) {
        this.classFilterProcessor = classFilterProcessor;
    }

}
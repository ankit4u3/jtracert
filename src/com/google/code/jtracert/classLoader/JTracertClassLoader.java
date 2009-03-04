package com.google.code.jtracert.classLoader;

import com.google.code.jtracert.classFilter.ClassFilterProcessor;
import com.google.code.jtracert.classFilter.ClassFilterable;
import com.google.code.jtracert.config.InstrumentationProperties;
import com.google.code.jtracert.instrument.ByteCodeTransformException;
import com.google.code.jtracert.instrument.ConfigurableTransformer;
import com.google.code.jtracert.instrument.JTracertByteCodeTransformer;
import com.google.code.jtracert.instrument.JTracertByteCodeTransformerFactory;
import com.google.code.jtracert.instrument.impl.adapter.JTracertByteCodeTransformerAdapter;

import java.io.IOException;
import java.io.InputStream;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 * @author Dmitry.Bedrin@gmail.com
 */
public class JTracertClassLoader
        extends ClassLoader
        implements ConfigurableTransformer, ClassFilterable {

    private InstrumentationProperties instrumentationProperties;
    private ClassFilterProcessor classFilterProcessor;

    /**
     *
     */
    public JTracertClassLoader() {
        super();
        this.classFilterProcessor = new ClassFilterProcessor();
    }

    /**
     *
     * @param instrumentationProperties
     */
    public JTracertClassLoader(InstrumentationProperties instrumentationProperties) {
        super();
        this.instrumentationProperties = instrumentationProperties;
        this.classFilterProcessor = new ClassFilterProcessor();
    }

    /**
     *
     * @param classFilterProcessor
     */
    public JTracertClassLoader(ClassFilterProcessor classFilterProcessor) {
        super();
        this.classFilterProcessor = classFilterProcessor;
    }

    /**
     *
     * @param parentClassLoader
     */
    public JTracertClassLoader(ClassLoader parentClassLoader) {
        super(parentClassLoader);
        this.classFilterProcessor = new ClassFilterProcessor();
    }

    /**
     *
     * @param parentClassLoader
     * @param instrumentationProperties
     */
    public JTracertClassLoader(ClassLoader parentClassLoader, InstrumentationProperties instrumentationProperties) {
        super(parentClassLoader);
        this.instrumentationProperties = instrumentationProperties;
        this.classFilterProcessor = new ClassFilterProcessor();
    }

    /**
     *
     * @param parentClassLoader
     * @param classFilterProcessor
     */
    public JTracertClassLoader(ClassLoader parentClassLoader, ClassFilterProcessor classFilterProcessor) {
        super(parentClassLoader);
        this.classFilterProcessor = classFilterProcessor;
    }

    /**
     *
     * @param parentClassLoader
     * @param classFilterProcessor
     * @param instrumentationProperties
     */
    public JTracertClassLoader(ClassLoader parentClassLoader, ClassFilterProcessor classFilterProcessor, InstrumentationProperties instrumentationProperties) {
        super(parentClassLoader);
        this.classFilterProcessor = classFilterProcessor;
        this.instrumentationProperties = instrumentationProperties;
    }

    /**
     *
     * @param name
     * @param resolve
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {

        Class clazz;

        try {
            clazz = findClass(name);
        } catch (ClassNotFoundException e) {
            return super.loadClass(name, resolve);
        }

        if (null == clazz) {
            return super.loadClass(name, resolve);
        } else {
            if (resolve) {
                resolveClass(clazz);
            }
            return clazz;
        }

    }

    /**
     *
     * @param name
     * @return
     */
    @Deprecated
    private boolean checkClassName(String name) {
        return name.startsWith("java.");
    }

    /**
     *
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {

        if (checkClassName(name)) return null;

        ClassFilterProcessor classFilterProcessor = getClassFilterProcessor();
        if (null != classFilterProcessor) {
            if (!classFilterProcessor.processClass(name, this)) {
                return null;
            }
        }

        JTracertByteCodeTransformer jTracertByteCodeTransformer =
                JTracertByteCodeTransformerFactory.getJTracertByteCodeTransformer(
                        getInstrumentationProperties());

        JTracertByteCodeTransformerAdapter jTracertByteCodeTransformerAdapter =
                new JTracertByteCodeTransformerAdapter(jTracertByteCodeTransformer);

        ClassLoader parentClassLoader = getParentOrSystemClassLoader();

        InputStream byteCodeInputStream = null;
        byte[] transformedByteArray;

        try {
            byteCodeInputStream = parentClassLoader.getResourceAsStream(convertClassNameToResourceName(name));
            transformedByteArray = jTracertByteCodeTransformerAdapter.transform(byteCodeInputStream);
        } catch (ByteCodeTransformException e) {
            throw new ClassNotFoundException("Error while instrumenting class " + name, e);
        } finally {
            if (null != byteCodeInputStream) {
                try {
                    byteCodeInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace(); // todo refactor this line
                }
            }
        }

        return defineClass(name, transformedByteArray, 0, transformedByteArray.length);
    }

    /**
     *
     * @todo move to ClassUtils class
     * @param name
     * @return
     */
    private String convertClassNameToResourceName(String name) {
        return name.replace('.','/') + ".class";
    }

    /**
     *
     * @return
     */
    private ClassLoader getParentOrSystemClassLoader() {

        ClassLoader parentClassLoader = getParent();

        if (null == parentClassLoader) {
            return getSystemClassLoader();
        } else {
            return parentClassLoader;
        }

    }

    /**
     *
     * @return
     */
    public InstrumentationProperties getInstrumentationProperties() {
        return instrumentationProperties;
    }

    /**
     *
     * @param instrumentationProperties
     */
    public void setInstrumentationProperties(InstrumentationProperties instrumentationProperties) {
        this.instrumentationProperties = instrumentationProperties;
    }

    /**
     *
     * @return
     */
    public ClassFilterProcessor getClassFilterProcessor() {
        return classFilterProcessor;
    }

    /**
     * 
     * @param classFilterProcessor
     */
    public void setClassFilterProcessor(ClassFilterProcessor classFilterProcessor) {
        this.classFilterProcessor = classFilterProcessor;
    }

}

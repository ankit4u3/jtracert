package com.google.code.jtracert.config;

import java.io.Serializable;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class InstrumentationProperties implements Serializable {

    private final static String CLASS_NAME_REGEX = "classNameRegEx";
    private final static String VERBOSE = "verboseInstrumentation";
    private final static String DUMP_TRANSFORMED_CLASSES = "dumpTransformedClasses";

    private String classNameRegEx;
    private boolean verbose;
    private boolean dumpTransformedClasses;

    static final long serialVersionUID = -2208856375496944037L;

    /**
     *
     */
    public InstrumentationProperties() {

    }

    /**
     * @return
     */
    public static InstrumentationProperties loadFromSystemProperties() {
        InstrumentationProperties instrumentationProperties
                = new InstrumentationProperties();

        String classNameRegEx = System.getProperty(CLASS_NAME_REGEX);
        if (null != classNameRegEx) {
            instrumentationProperties.setClassNameRegEx(classNameRegEx);
        }

        String verboseInstrumentation = System.getProperty(VERBOSE);
        if (null != verboseInstrumentation) {
            instrumentationProperties.setVerbose(Boolean.valueOf(verboseInstrumentation));
        } else {
            instrumentationProperties.setVerbose(false);
        }

        String dumpTransformedClasses = System.getProperty(DUMP_TRANSFORMED_CLASSES);
        if (null != dumpTransformedClasses) {
            instrumentationProperties.setDumpTransformedClasses(Boolean.valueOf(dumpTransformedClasses));
        } else {
            instrumentationProperties.setDumpTransformedClasses(false);
        }

        return instrumentationProperties;
    }

    /**
     * @return
     */
    public String getClassNameRegEx() {
        return classNameRegEx;
    }

    /**
     * @param classNameRegEx
     */
    public void setClassNameRegEx(String classNameRegEx) {
        this.classNameRegEx = classNameRegEx;
    }

    /**
     * @return
     */
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * @param verbose
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * @return
     */
    public boolean isDumpTransformedClasses() {
        return dumpTransformedClasses;
    }

    /**
     * @param dumpTransformedClasses
     */
    public void setDumpTransformedClasses(boolean dumpTransformedClasses) {
        this.dumpTransformedClasses = dumpTransformedClasses;
    }

}
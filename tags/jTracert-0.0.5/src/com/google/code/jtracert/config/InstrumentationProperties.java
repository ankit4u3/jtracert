package com.google.code.jtracert.config;

import java.io.Serializable;

/**
 * @author Dmitry Bedrin
 */
public class InstrumentationProperties implements Serializable {

    private final static String CLASS_NAME_REGEX = "classNameRegEx";
    private final static String VERBOSE = "verboseInstrumentation";
    private final static String DUMP_TRANSFORMED_CLASSES = "dumpTransformedClasses";

    private String classNameRegEx;
    private boolean verbose;
    private boolean dumpTransformedClasses;

    private InstrumentationProperties() {

    }

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

    public String getClassNameRegEx() {
        return classNameRegEx;
    }

    public void setClassNameRegEx(String classNameRegEx) {
        this.classNameRegEx = classNameRegEx;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public boolean isDumpTransformedClasses() {
        return dumpTransformedClasses;
    }

    public void setDumpTransformedClasses(boolean dumpTransformedClasses) {
        this.dumpTransformedClasses = dumpTransformedClasses;
    }
}

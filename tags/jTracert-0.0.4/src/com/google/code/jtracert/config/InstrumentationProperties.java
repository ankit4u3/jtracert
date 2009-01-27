package com.google.code.jtracert.config;

import java.io.Serializable;

/**
 * @author Dmitry Bedrin
 */
public class InstrumentationProperties implements Serializable {

    public final static String CLASS_NAME_REGEX = "classNameRegEx";
    public final static String VERBOSE = "verboseInstrumentation";

    private String classNameRegEx;
    private boolean verbose;

    public InstrumentationProperties() {

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

}

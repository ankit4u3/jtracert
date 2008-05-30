package com.google.code.jtracert.config;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author Dmitry Bedrin
 */
public class InstrumentationProperties implements Serializable {

    public final static String COLLECT_ARGUMENT_VALUES_PROPERTY_NAME = "collectArgumentValues";
    public final static String VERBOSE_PROPERTY_NAME = "verbose";

    private boolean collectArgumentValues;
    private boolean verbose;

    public InstrumentationProperties() {

    }

    public static InstrumentationProperties loadFromProperties(Properties properties) {

        InstrumentationProperties instrumentationProperties = new InstrumentationProperties();

        if (properties.containsKey(COLLECT_ARGUMENT_VALUES_PROPERTY_NAME)) {
            instrumentationProperties.setCollectArgumentValues(true);
        }

        if (properties.containsKey(VERBOSE_PROPERTY_NAME)) {
            instrumentationProperties.setVerbose(true);
        }

        return instrumentationProperties;

    }

    public boolean isCollectArgumentValues() {
        return collectArgumentValues;
    }

    public void setCollectArgumentValues(boolean collectArgumentValues) {
        this.collectArgumentValues = collectArgumentValues;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

}

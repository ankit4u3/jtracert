package com.google.code.jtracert.model;

import java.io.Serializable;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public abstract class ObjectDump implements Serializable {

    /**
     *
     */
    private String className;

    /**
     *
     */
    private String variableName;

    /**
     *
     * @return
     */
    public String getClassName() {
        return className;
    }

    /**
     *
     * @param className
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     *
     * @return
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     *
     * @param variableName
     */
    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    /**
     * 
     * @return
     */
    @Override
    public String toString() {
        return "ObjectDump{" +
                "className='" + className + '\'' +
                ", variableName='" + variableName + '\'' +
                '}';
    }
}

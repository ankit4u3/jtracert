package com.google.code.jtracert.model;

import java.util.List;
import java.util.ArrayList;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class ComplexObjectDump extends ObjectDump {

    /**
     *
     */
    private List<ObjectDump> fieldDumps = new ArrayList<ObjectDump>();

    /**
     *
     */
    private int hashCode;

    /**
     *
     * @return
     */
    public List<ObjectDump> getFieldDumps() {
        return fieldDumps;
    }

    /**
     *
     * @param fieldDumps
     */
    public void setFieldDumps(List<ObjectDump> fieldDumps) {
        this.fieldDumps = fieldDumps;
    }

    /**
     *
     * @return
     */
    public int getHashCode() {
        return hashCode;
    }

    /**
     *
     * @param hashCode
     */
    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    /**
     * 
     * @return
     */
    @Override
    public String toString() {
        return "ComplexObjectDump{" +
                "fieldDumps=" + fieldDumps +
                ", hashCode=" + hashCode +
                "} " + super.toString();
    }
    
}

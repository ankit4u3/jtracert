package com.google.code.jtracert.model;

import java.io.Serializable;

/**
 * @author Dmitry Bedrin
 */
public class JTracertObjectCompanion implements Serializable {

    private final int hashCode;

    public JTracertObjectCompanion(int hashCode) {
        this.hashCode = hashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JTracertObjectCompanion)) return false;

        JTracertObjectCompanion that = (JTracertObjectCompanion) o;

        return hashCode == that.hashCode;

    }

    @Override
    public int hashCode() {
        return hashCode;
    }
    
}
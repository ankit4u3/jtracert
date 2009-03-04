package com.google.code.jtracert.model;

import java.io.Serializable;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
@Deprecated
public class JTracertObjectCompanion implements Serializable {

    static final long serialVersionUID = -8438577240614871138L;

    private final int hashCode;

    /**
     * @param hashCode
     */
    public JTracertObjectCompanion(int hashCode) {
        this.hashCode = hashCode;
    }

    /**
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JTracertObjectCompanion)) return false;

        JTracertObjectCompanion that = (JTracertObjectCompanion) o;

        return hashCode == that.hashCode;

    }

    /**
     * @return
     */
    @Override
    public int hashCode() {
        return hashCode;
    }

}

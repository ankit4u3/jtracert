package com.google.code.jtracert.classFilter;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 * @author Dmitry.Bedrin@gmail.com
 */
public enum FilterAction {

    INHERIT,
    DENY,
    ALLOW;

    /**
     *
     * @param fa
     * @return
     */
    public FilterAction apply(FilterAction fa) {
        switch (fa) {
            case INHERIT:
                return this;
            case DENY:
                return DENY;
            case ALLOW:
                return ALLOW;
            default:
                return this;
        }
    }


}

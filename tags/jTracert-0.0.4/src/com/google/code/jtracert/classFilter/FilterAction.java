package com.google.code.jtracert.classFilter;

/**
 * @author Dmitry Bedrin
 */
public enum FilterAction {

    INHERIT,
    DENY,
    ALLOW;

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

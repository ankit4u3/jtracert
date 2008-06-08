package com.google.code.jtracert.classFilter.impl;

import com.google.code.jtracert.classFilter.FilterAction;
import static com.google.code.jtracert.classFilter.FilterAction.INHERIT;

/**
 * @author Dmitry Bedrin
 */
public class InheritClassFilter extends DefaultActionClassFilterAdapter {

    protected FilterAction getDefaultFilterAction() {
        return INHERIT;
    }

}

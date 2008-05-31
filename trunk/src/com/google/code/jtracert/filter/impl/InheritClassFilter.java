package com.google.code.jtracert.filter.impl;

import com.google.code.jtracert.filter.FilterAction;
import static com.google.code.jtracert.filter.FilterAction.INHERIT;

/**
 * @author Dmitry Bedrin
 */
public class InheritClassFilter extends DefaultActionClassFilterAdapter {

    protected FilterAction getDefaultFilterAction() {
        return INHERIT;
    }

}

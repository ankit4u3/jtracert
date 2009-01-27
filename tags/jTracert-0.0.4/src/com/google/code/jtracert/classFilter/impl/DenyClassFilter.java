package com.google.code.jtracert.classFilter.impl;

import com.google.code.jtracert.classFilter.FilterAction;
import static com.google.code.jtracert.classFilter.FilterAction.DENY;

/**
 * @author Dmitry Bedrin
 */
public class DenyClassFilter extends DefaultActionClassFilterAdapter {

    protected FilterAction getDefaultFilterAction() {
        return DENY;
    }

}
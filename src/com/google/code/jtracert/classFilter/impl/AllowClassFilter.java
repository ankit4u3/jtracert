package com.google.code.jtracert.classFilter.impl;

import com.google.code.jtracert.classFilter.FilterAction;
import static com.google.code.jtracert.classFilter.FilterAction.ALLOW;

/**
 * @author Dmitry Bedrin
 */
public class AllowClassFilter extends DefaultActionClassFilterAdapter {

    protected FilterAction getDefaultFilterAction() {
        return ALLOW;
    }

}
package com.google.code.jtracert.filter.impl;

import com.google.code.jtracert.filter.FilterAction;
import static com.google.code.jtracert.filter.FilterAction.ALLOW;

/**
 * @author Dmitry Bedrin
 */
public class AllowClassFilter extends DefaultActionClassFilterAdapter {

    protected FilterAction getDefaultFilterAction() {
        return ALLOW;
    }

}
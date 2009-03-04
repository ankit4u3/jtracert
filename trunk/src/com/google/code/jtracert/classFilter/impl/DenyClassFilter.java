package com.google.code.jtracert.classFilter.impl;

import com.google.code.jtracert.classFilter.FilterAction;
import static com.google.code.jtracert.classFilter.FilterAction.DENY;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class DenyClassFilter extends DefaultActionClassFilterAdapter {

    /**
     * @return
     */
    protected FilterAction getDefaultFilterAction() {
        return DENY;
    }

}
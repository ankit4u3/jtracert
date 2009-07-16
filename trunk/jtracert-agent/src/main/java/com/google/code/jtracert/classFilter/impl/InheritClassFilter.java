package com.google.code.jtracert.classFilter.impl;

import com.google.code.jtracert.classFilter.FilterAction;
import static com.google.code.jtracert.classFilter.FilterAction.INHERIT;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class InheritClassFilter extends DefaultActionClassFilterAdapter {

    /**
     * @return
     */
    protected FilterAction getDefaultFilterAction() {
        return INHERIT;
    }

}

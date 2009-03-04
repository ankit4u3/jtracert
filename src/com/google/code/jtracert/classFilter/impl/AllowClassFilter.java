package com.google.code.jtracert.classFilter.impl;

import com.google.code.jtracert.classFilter.FilterAction;
import static com.google.code.jtracert.classFilter.FilterAction.ALLOW;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class AllowClassFilter extends DefaultActionClassFilterAdapter {

    /**
     * @return
     */
    protected FilterAction getDefaultFilterAction() {
        return ALLOW;
    }

}
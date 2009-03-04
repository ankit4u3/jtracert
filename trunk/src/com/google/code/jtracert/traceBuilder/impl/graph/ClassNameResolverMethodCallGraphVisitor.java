package com.google.code.jtracert.traceBuilder.impl.graph;

import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.model.MethodCallVisitor;

import java.util.*;

/**
 * @author dmitry.bedrin
 */
public class ClassNameResolverMethodCallGraphVisitor implements MethodCallVisitor<Object> {

    private Map<String, Collection<String>> classNameMap = new HashMap<String, Collection<String>>();
    private boolean renaming = false;

    public void setRenaming() {
        renaming = true;
    }

    public Object visit(MethodCall methodCall) {

        String longClassName = methodCall.getRealClassName();
        String shortClassName =
                -1 == longClassName.lastIndexOf('.') ?
                        longClassName :
                        longClassName.substring(longClassName.lastIndexOf('.') + 1);

        if (renaming) {
            if (1 == classNameMap.get(shortClassName).size()) {
                methodCall.setClassName(shortClassName);
            }
        } else {
            if (!classNameMap.containsKey(shortClassName)) {
                classNameMap.put(shortClassName, new HashSet<String>(1));
            }
            classNameMap.get(shortClassName).add(longClassName);
        }

        for (MethodCall callee : methodCall.getCallees()) {
            callee.accept(this);
        }

        return null;
    }

}
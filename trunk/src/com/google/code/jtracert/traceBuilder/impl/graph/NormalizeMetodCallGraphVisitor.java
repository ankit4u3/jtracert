package com.google.code.jtracert.traceBuilder.impl.graph;

import com.google.code.jtracert.model.MethodCall;

import java.util.ListIterator;

/**
 * @author dmitry.bedrin
 */
public class NormalizeMetodCallGraphVisitor implements MethodCallVisitor<Object> {

    public Object visit(MethodCall methodCall) {

        int previousCaleeHashCode = 0;

        ListIterator<MethodCall> caleeIterator = methodCall.getCallees().listIterator();
        while (caleeIterator.hasNext()) {
            MethodCall calee;
            calee = caleeIterator.next();
            int currentCaleeHashCode = calee.accept(new HashCodeBuilderMethodCallGraphVisitor());
            if (previousCaleeHashCode == currentCaleeHashCode) {

                caleeIterator.previous();
                caleeIterator.previous().incrementCallCount();
                caleeIterator.next();
                caleeIterator.next();
                caleeIterator.remove();

            }
            previousCaleeHashCode = currentCaleeHashCode;
        }

        for (MethodCall callee : methodCall.getCallees()) {
            callee.accept(this);
        }

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

package com.google.code.jtracert.traceBuilder.impl.graph;

import com.google.code.jtracert.model.MethodCall;
import junit.framework.TestCase;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 * @author Dmitry.Bedrin@gmail.com
 */
public class NormalizeMetodCallGraphVisitorTest extends TestCase {

    /**
     * 
     */
    public void testNormalizeTwoSobsequences() {

        MethodCall parentMethodCall = new MethodCall("ParentClass","main","()V");

        MethodCall childMethod1 = new MethodCall("ChildClass","childMethod","()V");
        MethodCall childMethod11 = new MethodCall("ChildClass1","childMethod1","()V");
        MethodCall childMethod12 = new MethodCall("ChildClass1","childMethod2","()V");
        childMethod1.addCallee(childMethod11);
        childMethod1.addCallee(childMethod12);

        MethodCall childMethod2 = new MethodCall("ChildClass","childMethod","()V");
        MethodCall childMethod21 = new MethodCall("ChildClass1","childMethod1","()V");
        MethodCall childMethod22 = new MethodCall("ChildClass1","childMethod2","()V");
        childMethod2.addCallee(childMethod21);
        childMethod2.addCallee(childMethod22);

        parentMethodCall.addCallee(childMethod1);
        parentMethodCall.addCallee(childMethod2);

        assertEquals(2,parentMethodCall.getCallees().size());

        parentMethodCall.accept(new NormalizeMetodCallGraphVisitor());

        assertEquals(1,parentMethodCall.getCallees().size());


    }

}

package com.google.code.jtracert.traceBuilder.impl.graph;

import com.google.code.jtracert.model.MethodCall;
import junit.framework.TestCase;

/**
 * @author dmitry.bedrin
 */
public class NormalizeMetodCallGraphVisitorTest extends TestCase {

    public void testNormalizeTwoSobsequences() {

        MethodCall parentMethodCall = new MethodCall("ParentClass","main","()V",null);

        MethodCall childMethod1 = new MethodCall("ChildClass","childMethod","()V",null);
        MethodCall childMethod11 = new MethodCall("ChildClass1","childMethod1","()V",null);
        MethodCall childMethod12 = new MethodCall("ChildClass1","childMethod2","()V",null);
        childMethod1.addCallee(childMethod11);
        childMethod1.addCallee(childMethod12);

        MethodCall childMethod2 = new MethodCall("ChildClass","childMethod","()V",null);
        MethodCall childMethod21 = new MethodCall("ChildClass1","childMethod1","()V",null);
        MethodCall childMethod22 = new MethodCall("ChildClass1","childMethod2","()V",null);
        childMethod2.addCallee(childMethod21);
        childMethod2.addCallee(childMethod22);

        parentMethodCall.addCallee(childMethod1);
        parentMethodCall.addCallee(childMethod2);

        assertEquals(2,parentMethodCall.getCallees().size());

        parentMethodCall.accept(new NormalizeMetodCallGraphVisitor());

        assertEquals(1,parentMethodCall.getCallees().size());


    }

}

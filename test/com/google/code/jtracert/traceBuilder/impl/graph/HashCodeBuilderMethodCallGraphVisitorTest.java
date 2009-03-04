package com.google.code.jtracert.traceBuilder.impl.graph;

import com.google.code.jtracert.model.MethodCall;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 * @author Dmitry.Bedrin@gmail.com
 */
public class HashCodeBuilderMethodCallGraphVisitorTest extends TestCase {

    /**
     *
     */
    @Before
    public void setUp() {

    }

    /**
     * 
     */
    @Test
    public void testHashCodeEquality() {

        MethodCall methodCall1 = new MethodCall();

        methodCall1.setRealClassName("EntryClass");
        methodCall1.setMethodName("main");

        MethodCall methodCall2 = new MethodCall();
        methodCall2.setRealClassName("class1");
        methodCall2.setMethodName("method1");

        MethodCall methodCall3 = new MethodCall();
        methodCall3.setRealClassName("class2");
        methodCall3.setMethodName("method2");

        methodCall1.addCallee(methodCall2);
        methodCall1.addCallee(methodCall3);

        HashCodeBuilderMethodCallGraphVisitor hashCodeBuilderMethodCallGraphVisitor = new HashCodeBuilderMethodCallGraphVisitor();

        int hashCode = methodCall1.accept(hashCodeBuilderMethodCallGraphVisitor);

        MethodCall anotherMethodCall1 = new MethodCall();

        anotherMethodCall1.setRealClassName("EntryClass");
        anotherMethodCall1.setMethodName("main");

        MethodCall anotherMethodCall2 = new MethodCall();
        anotherMethodCall2.setRealClassName("class1");
        anotherMethodCall2.setMethodName("method1");

        MethodCall anotherMethodCall3 = new MethodCall();
        anotherMethodCall3.setRealClassName("class2");
        anotherMethodCall3.setMethodName("method2");

        anotherMethodCall1.addCallee(methodCall2);
        anotherMethodCall1.addCallee(methodCall3);

        HashCodeBuilderMethodCallGraphVisitor anotherHashCodeBuilderMethodCallGraphVisitor = new HashCodeBuilderMethodCallGraphVisitor();

        int anotherHashCode = anotherMethodCall1.accept(anotherHashCodeBuilderMethodCallGraphVisitor);

        assertEquals(hashCode, anotherHashCode);

    }

}

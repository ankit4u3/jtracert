package com.google.code.jtracert.traceBuilder.impl.graph;

import org.junit.Test;
import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.model.JTracertObjectCompanion;
import junit.framework.TestCase;

/**
 * @author dmitry.bedrin
 */
public class NormalizeMetodCallGraphVisitorTest extends TestCase {

    @Test
    public void testNormalizeSimpleCallGraphRemoveOneDublicate() {

        MethodCall parentMethodCall = new MethodCall("ParentClass","main","()V",null);

        MethodCall childMethod1 = new MethodCall("ChildClass1","childMethod1","()V",new JTracertObjectCompanion());
        MethodCall childMethod2 = new MethodCall("ChildClass1","childMethod1","()V",new JTracertObjectCompanion());

        parentMethodCall.addCallee(childMethod1);
        parentMethodCall.addCallee(childMethod2);

        assertEquals(2,parentMethodCall.getCallees().size());

        parentMethodCall.accept(new NormalizeMetodCallGraphVisitor());

        assertEquals(1,parentMethodCall.getCallees().size());

    }

    @Test
    public void testNormalizeSimpleNestedCallGraphRemoveOneDublicate() {

        MethodCall parentMethodCall = new MethodCall("ParentClass","main","()V",null);

        MethodCall childMethod1 = new MethodCall("ChildClass1","childMethod1","()V",new JTracertObjectCompanion());
        MethodCall childMethod11 = new MethodCall("ChildClass2","childMethod1","()V",new JTracertObjectCompanion());
        MethodCall childMethod12 = new MethodCall("ChildClass2","childMethod2","()V",new JTracertObjectCompanion());
        childMethod1.addCallee(childMethod11);
        childMethod1.addCallee(childMethod12);

        MethodCall childMethod2 = new MethodCall("ChildClass1","childMethod1","()V",new JTracertObjectCompanion());
        MethodCall childMethod21 = new MethodCall("ChildClass2","childMethod1","()V",new JTracertObjectCompanion());
        MethodCall childMethod22 = new MethodCall("ChildClass2","childMethod2","()V",new JTracertObjectCompanion());
        childMethod2.addCallee(childMethod21);
        childMethod2.addCallee(childMethod22);

        parentMethodCall.addCallee(childMethod1);
        parentMethodCall.addCallee(childMethod2);

        assertEquals(2,parentMethodCall.getCallees().size());

        parentMethodCall.accept(new NormalizeMetodCallGraphVisitor());

        assertEquals(1,parentMethodCall.getCallees().size());

    }

    @Test
    public void testNormalizeSimpleNestedChildCallGraphRemoveOneDublicate() {

        MethodCall parentMethodCall = new MethodCall("ParentClass","main","()V",null);

        MethodCall childMethodCall = new MethodCall("ChildClass","main","()V",null);

        parentMethodCall.addCallee(childMethodCall);

        MethodCall childMethod1 = new MethodCall("ChildClass1","childMethod1","()V",new JTracertObjectCompanion());
        MethodCall childMethod11 = new MethodCall("ChildClass2","childMethod1","()V",new JTracertObjectCompanion());
        MethodCall childMethod12 = new MethodCall("ChildClass2","childMethod2","()V",new JTracertObjectCompanion());
        childMethod1.addCallee(childMethod11);
        childMethod1.addCallee(childMethod12);

        MethodCall childMethod2 = new MethodCall("ChildClass1","childMethod1","()V",new JTracertObjectCompanion());
        MethodCall childMethod21 = new MethodCall("ChildClass2","childMethod1","()V",new JTracertObjectCompanion());
        MethodCall childMethod22 = new MethodCall("ChildClass2","childMethod2","()V",new JTracertObjectCompanion());
        childMethod2.addCallee(childMethod21);
        childMethod2.addCallee(childMethod22);

        childMethodCall.addCallee(childMethod1);
        childMethodCall.addCallee(childMethod2);

        assertEquals(2,childMethodCall.getCallees().size());

        parentMethodCall.accept(new NormalizeMetodCallGraphVisitor());

        assertEquals(1,childMethodCall.getCallees().size());

    }

    @Test
    public void testNormalizeSimpleCallGraphLeaveSame() {

        MethodCall parentMethodCall = new MethodCall("ParentClass","main","()V",null);

        MethodCall childMethod1 = new MethodCall("ChildClass1","childMethod1","()V",new JTracertObjectCompanion());
        MethodCall childMethod2 = new MethodCall("ChildClass1","childMethod2","()V",new JTracertObjectCompanion());

        parentMethodCall.addCallee(childMethod1);
        parentMethodCall.addCallee(childMethod2);

        assertEquals(2,parentMethodCall.getCallees().size());

        parentMethodCall.accept(new NormalizeMetodCallGraphVisitor());

        assertEquals(2,parentMethodCall.getCallees().size());

    }

}

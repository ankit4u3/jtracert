package com.google.code.jtracert.traceBuilder.impl;

import junit.framework.TestCase;
import org.junit.Test;
import com.google.code.jtracert.model.ObjectDump;
import com.google.code.jtracert.model.ComplexObjectDump;

public class ObjectDumpBuilderTest extends TestCase {

    private class TestClass1 {
        private TestClass1 field1;
        private int field2;
        public String field3;

        private TestClass1() {
        }

        private TestClass1(TestClass1 field1, int field2, String field3) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
        }
    }

    @Test
    public void testDump() throws Exception {
        try {
            TestClass1 testClass1 = new TestClass1(new TestClass1(null,5,"bla"),10,"blabla");

            ObjectDumpBuilder objectDumpBuilder = new ObjectDumpBuilder();

            ComplexObjectDump objectDump = objectDumpBuilder.dump(testClass1);

            assertEquals(objectDump.getHashCode(), System.identityHashCode(testClass1));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

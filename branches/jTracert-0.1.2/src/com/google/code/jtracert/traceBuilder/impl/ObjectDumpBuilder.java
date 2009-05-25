package com.google.code.jtracert.traceBuilder.impl;

import com.google.code.jtracert.model.ComplexObjectDump;
import com.google.code.jtracert.model.SimpleObjectDump;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.HashMap;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class ObjectDumpBuilder {

    /**
     *
     */
    private Map<Integer, ComplexObjectDump> objectReferencesMap =
            new HashMap<Integer, ComplexObjectDump>();

    /**
     *
     * @param o
     * @return
     * @throws Exception
     */
    public ComplexObjectDump dump(Object o) throws Exception {

        ComplexObjectDump dump = new ComplexObjectDump();

        Class clazz = o.getClass();

        dump.setClassName(clazz.getCanonicalName());
        dump.setHashCode(System.identityHashCode(o));

        for (Field field : clazz.getDeclaredFields()) {

            synchronized(o) {

                boolean wasAccessible = ensureAccessible(field);

                Class fieldClass = field.getType();

                if (fieldClass.equals(boolean.class)) {
                    dump.getFieldDumps().add(createSimpleObjectDump(field,
                            Boolean.toString(field.getBoolean(o))));
                } else if (fieldClass.equals(byte.class)) {
                    dump.getFieldDumps().add(createSimpleObjectDump(field,
                            Byte.toString(field.getByte(o))));
                } else if (fieldClass.equals(int.class)) {
                    dump.getFieldDumps().add(createSimpleObjectDump(field,
                            Integer.toString(field.getInt(o))));
                } else if (fieldClass.equals(long.class)) {
                    dump.getFieldDumps().add(createSimpleObjectDump(field,
                            Long.toString(field.getLong(o))));
                } else if (fieldClass.equals(float.class)) {
                    dump.getFieldDumps().add(createSimpleObjectDump(field,
                            Float.toString(field.getFloat(o))));
                } else if (fieldClass.equals(double.class)) {
                    dump.getFieldDumps().add(createSimpleObjectDump(field,
                            Double.toString(field.getDouble(o))));
                } else if (fieldClass.equals(String.class)) {
                    dump.getFieldDumps().add(createSimpleObjectDump(field,
                            null == field.get(o) ? null :
                            new String(((String)field.get(o)).getBytes())
                    ));
                } else {
                    Object object = field.get(o);

                    if (null == object) {
                        dump.getFieldDumps().add(createSimpleObjectDump(field,null));
                    } else {
                        int hashCode = System.identityHashCode(object);

                        if (!objectReferencesMap.containsKey(hashCode)) {
                            ComplexObjectDump complexObjectDump = dump(object);
                            complexObjectDump.setClassName(field.getDeclaringClass().getCanonicalName());
                            complexObjectDump.setVariableName(field.getName());
                            complexObjectDump.setHashCode(hashCode);
                            objectReferencesMap.put(hashCode, complexObjectDump);
                        }

                        dump.getFieldDumps().add(objectReferencesMap.get(hashCode));
                    }

                }

                field.setAccessible(wasAccessible);

            }

        }

        return dump;

    }

    /**
     *
     * @param field
     * @return
     */
    private static boolean ensureAccessible(Field field) {
        boolean wasAccessible = field.isAccessible();
        field.setAccessible(true);
        return wasAccessible;
    }

    /**
     * 
     * @param field
     * @param value
     * @return
     */
    private static SimpleObjectDump createSimpleObjectDump(Field field, String value) {

        SimpleObjectDump fieldDump = new SimpleObjectDump();

        fieldDump.setClassName(field.getDeclaringClass().getCanonicalName());
        fieldDump.setVariableName(field.getName());
        fieldDump.setValue(value);

        return fieldDump;
        
    }

}

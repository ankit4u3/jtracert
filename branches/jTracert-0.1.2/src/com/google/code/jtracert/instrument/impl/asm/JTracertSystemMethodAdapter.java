package com.google.code.jtracert.instrument.impl.asm;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class JTracertSystemMethodAdapter extends AdviceAdapter implements Opcodes {

    String name;
    String desc;
    String className;

    public JTracertSystemMethodAdapter(MethodVisitor methodVisitor, int acc, String name, String desc, String className) {
        super(methodVisitor,acc,name,desc);
        this.name = name;
        this.desc = desc;
        this.className = className;
    }

    @Override
    protected void onMethodEnter() {

        try {

            super.visitMethodInsn(INVOKESTATIC, "java/lang/ClassLoader", "getSystemClassLoader", "()Ljava/lang/ClassLoader;");
            super.visitLdcInsn("com.google.code.jtracert.traceBuilder.MethodCallTraceBuilderFactory");
            super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/ClassLoader", "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;");

            int classVar = newLocal(Type.getType(Class.class));

            super.visitInsn(DUP);
            super.visitVarInsn(ASTORE, classVar);

            super.visitLdcInsn("instrumenting");
            super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;");
            super.visitInsn(ACONST_NULL);
            super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Field", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
            super.visitTypeInsn(CHECKCAST, "java/lang/ThreadLocal");

            int booleanThreadLocalVar = newLocal(Type.getType(ThreadLocal.class));

            super.visitInsn(DUP);
            super.visitVarInsn(ASTORE, booleanThreadLocalVar);


            super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/ThreadLocal", "get", "()Ljava/lang/Object;");
            super.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
            super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z");

            Label buildingTraceLabel = new Label();

            super.visitJumpInsn(IFNE, buildingTraceLabel);

            super.visitVarInsn(ALOAD, booleanThreadLocalVar);
            super.visitFieldInsn(GETSTATIC, "java/lang/Boolean", "TRUE", "Ljava/lang/Boolean;");
            super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/ThreadLocal", "set", "(Ljava/lang/Object;)V");
            super.visitVarInsn(ALOAD, classVar);
            super.visitLdcInsn("test");
            super.visitInsn(ICONST_0);
            super.visitTypeInsn(ANEWARRAY, "java/lang/Class");
            super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;");
            super.visitInsn(ACONST_NULL);
            super.visitInsn(ICONST_0);
            super.visitTypeInsn(ANEWARRAY, "java/lang/Object");
            super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;");
            super.visitInsn(POP);
            super.visitVarInsn(ALOAD, booleanThreadLocalVar);
            super.visitFieldInsn(GETSTATIC, "java/lang/Boolean", "FALSE", "Ljava/lang/Boolean;");
            super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/ThreadLocal", "set", "(Ljava/lang/Object;)V");

            super.visitLabel(buildingTraceLabel);

            /*super.visitMethodInsn(INVOKESTATIC, "java/lang/ClassLoader", "getSystemClassLoader", "()Ljava/lang/ClassLoader;");


            super.visitLdcInsn("com.google.code.jtracert.traceBuilder.MethodCallTraceBuilderFactory");
            super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/ClassLoader", "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;");

            super.visitLdcInsn("test");
            super.visitInsn(ICONST_0);
            super.visitTypeInsn(ANEWARRAY, "java/lang/Class");
            super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;");

            super.visitInsn(ACONST_NULL);
            super.visitInsn(ICONST_0);
            super.visitTypeInsn(ANEWARRAY, "java/lang/Object");
            super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;");
            super.visitInsn(POP);*/

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
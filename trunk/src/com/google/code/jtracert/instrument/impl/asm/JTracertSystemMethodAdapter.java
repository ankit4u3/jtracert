package com.google.code.jtracert.instrument.impl.asm;

import org.objectweb.asm.*;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class JTracertSystemMethodAdapter extends MethodAdapter implements Opcodes {

    String name;
    String desc;
    String className;

    public JTracertSystemMethodAdapter(MethodVisitor methodVisitor, int acc, String name, String desc, String className) {
        super(methodVisitor);
        this.name = name;
        this.desc = desc;
        this.className = className;
    }

    @Override
    public void visitCode() {

        super.visitCode();

        try {

            super.visitMethodInsn(INVOKESTATIC, "java/lang/ClassLoader", "getSystemClassLoader", "()Ljava/lang/ClassLoader;");


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
            super.visitInsn(POP);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
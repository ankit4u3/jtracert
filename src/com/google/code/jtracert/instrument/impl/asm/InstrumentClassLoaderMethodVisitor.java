package com.google.code.jtracert.instrument.impl.asm;

import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

public class InstrumentClassLoaderMethodVisitor extends MethodAdapter implements Opcodes {

    public InstrumentClassLoaderMethodVisitor(MethodVisitor methodVisitor) {
        super(methodVisitor);
    }

    @Override
    public void visitCode() {
        super.visitCode();

        mv.visitVarInsn(ALOAD, 1);
        mv.visitLdcInsn("com.google.code.jtracert");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z");
        Label l0 = new Label();
        mv.visitJumpInsn(IFEQ, l0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitLdcInsn("com.google.code.jtracert.samples");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z");
        mv.visitJumpInsn(IFNE, l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitTypeInsn(INSTANCEOF, "java/lang/ClassLoader");
        mv.visitJumpInsn(IFEQ, l0);
        /*mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "dumpStack", "()V");*/
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/ClassLoader", "getSystemClassLoader", "()Ljava/lang/ClassLoader;");
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/ClassLoader", "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;");
        mv.visitInsn(ARETURN);
        mv.visitLabel(l0);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
    }

}

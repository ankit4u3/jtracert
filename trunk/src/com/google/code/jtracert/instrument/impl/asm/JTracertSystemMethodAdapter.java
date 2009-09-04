package com.google.code.jtracert.instrument.impl.asm;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;
import com.google.code.jtracert.config.InstrumentationProperties;
import com.google.code.jtracert.util.ClassUtils;

import java.lang.reflect.Method;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class JTracertSystemMethodAdapter extends AdviceAdapter implements Opcodes {

    private final String className;
    private final String methodName;
    private final boolean isConstructor;

    private final Label startFinallyLabel = new Label();

    public JTracertSystemMethodAdapter(MethodVisitor mv, int access, String name, String desc, String className) {
        super(mv, access, name, desc);
        this.className = className;
        this.methodName = name;
        this.isConstructor = name.equals(ClassUtils.CONSTRUCTOR_METHOD_NAME);
    }

    /**
     *
     */
    @Override
    public void visitCode() {
        super.visitCode();
        mv.visitLabel(startFinallyLabel);
    }

    /**
     * @param maxStack
     * @param maxLocals
     */
    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        Label endFinallyLabel = new Label();
        mv.visitTryCatchBlock(startFinallyLabel, endFinallyLabel, endFinallyLabel, null);
        mv.visitLabel(endFinallyLabel);
        onFinally(ATHROW);
        mv.visitInsn(ATHROW);
        mv.visitMaxs(maxStack, maxLocals);
    }

    /**
     * @param opcode
     */
    @Override
    protected void onMethodExit(int opcode) {
        if (opcode != ATHROW) {
            onFinally(opcode);
        }
    }
    
    /**
     * @param opcode
     */
    private void onFinally(int opcode) {

        mv.visitMethodInsn(INVOKESTATIC, "java/lang/ClassLoader", "getSystemClassLoader", "()Ljava/lang/ClassLoader;");
        super.visitLdcInsn("com.google.code.jtracert.traceBuilder.MethodCallTraceBuilderFactory");
        super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/ClassLoader", "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;");
        super.visitLdcInsn("getMethodCallTraceBuilder");
        super.visitInsn(ACONST_NULL);
        super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;");
        super.visitInsn(ACONST_NULL);
        super.visitInsn(ACONST_NULL);
        super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;");

        int methodCallTraceBuilder = newLocal(Type.getType(Method.class));
        super.visitVarInsn(ASTORE, methodCallTraceBuilder);

        super.visitMethodInsn(INVOKESTATIC, "java/lang/ClassLoader", "getSystemClassLoader", "()Ljava/lang/ClassLoader;");
        super.visitLdcInsn("com.google.code.jtracert.traceBuilder.MethodCallTraceBuilder");
        super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/ClassLoader", "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;");
        super.visitLdcInsn("leave");
        super.visitInsn(ACONST_NULL);
        super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;");
        super.visitVarInsn(ALOAD, methodCallTraceBuilder);
        super.visitInsn(ICONST_0);
        super.visitTypeInsn(ANEWARRAY, "java/lang/Object");
        super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;");
        super.visitInsn(POP);


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

            onMethodEnterInternal(classVar);

            super.visitVarInsn(ALOAD, booleanThreadLocalVar);
            super.visitFieldInsn(GETSTATIC, "java/lang/Boolean", "FALSE", "Ljava/lang/Boolean;");
            super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/ThreadLocal", "set", "(Ljava/lang/Object;)V");

            super.visitLabel(buildingTraceLabel);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void onMethodEnterInternal(int methodCallTraceBuilderFactoryClass) {
        super.visitVarInsn(ALOAD, methodCallTraceBuilderFactoryClass);
        super.visitLdcInsn("getMethodCallTraceBuilder");
        super.visitInsn(ICONST_0);
        super.visitTypeInsn(ANEWARRAY, "java/lang/Class");
        super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;");
        super.visitInsn(ACONST_NULL);
        super.visitInsn(ICONST_0);
        super.visitTypeInsn(ANEWARRAY, "java/lang/Object");
        super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;");

        // MethodCallTraceBuilder instance is on stack

        int methodCallTraceBuilderLocalVar = newLocal(Type.getType(Object.class));
        super.visitVarInsn(ASTORE, methodCallTraceBuilderLocalVar);

        try {

            if ((ACC_STATIC & methodAccess) == 0) {

                super.visitMethodInsn(INVOKESTATIC, "java/lang/ClassLoader", "getSystemClassLoader", "()Ljava/lang/ClassLoader;");
                super.visitLdcInsn("com.google.code.jtracert.traceBuilder.MethodCallTraceBuilder");
                super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/ClassLoader", "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;");

                super.visitLdcInsn("enter");
                super.visitInsn(ICONST_4);
                super.visitTypeInsn(ANEWARRAY, "java/lang/Class");
                super.visitInsn(DUP);
                super.visitInsn(ICONST_0);
                super.visitLdcInsn(Type.getType("Ljava/lang/String;"));
                super.visitInsn(AASTORE);
                super.visitInsn(DUP);
                super.visitInsn(ICONST_1);
                super.visitLdcInsn(Type.getType("Ljava/lang/String;"));
                super.visitInsn(AASTORE);
                super.visitInsn(DUP);
                super.visitInsn(ICONST_2);
                super.visitLdcInsn(Type.getType("Ljava/lang/String;"));
                super.visitInsn(AASTORE);
                super.visitInsn(DUP);
                super.visitInsn(ICONST_3);
                super.visitLdcInsn(Type.getType("Ljava/lang/Object;"));
                super.visitInsn(AASTORE);

                super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;");

                super.visitVarInsn(ALOAD, methodCallTraceBuilderLocalVar);

                super.visitInsn(ICONST_4);
                super.visitTypeInsn(ANEWARRAY, "java/lang/Object");
                super.visitInsn(DUP);
                super.visitInsn(ICONST_0);
                super.visitLdcInsn(getClassName());
                super.visitInsn(AASTORE);
                super.visitInsn(DUP);
                super.visitInsn(ICONST_1);
                super.visitLdcInsn(getMethodName());
                super.visitInsn(AASTORE);
                super.visitInsn(DUP);
                super.visitInsn(ICONST_2);
                super.visitLdcInsn(getMethodDescriptor());
                super.visitInsn(AASTORE);
                super.visitInsn(DUP);
                super.visitInsn(ICONST_3);
                super.visitVarInsn(ALOAD, 0);
                super.visitInsn(AASTORE);


                super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;");
                super.visitInsn(POP);

            } else {

                super.visitMethodInsn(INVOKESTATIC, "java/lang/ClassLoader", "getSystemClassLoader", "()Ljava/lang/ClassLoader;");
                super.visitLdcInsn("com.google.code.jtracert.traceBuilder.MethodCallTraceBuilder");
                super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/ClassLoader", "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;");

                super.visitLdcInsn("enter");
                super.visitInsn(ICONST_4);
                super.visitTypeInsn(ANEWARRAY, "java/lang/Class");
                super.visitInsn(DUP);
                super.visitInsn(ICONST_0);
                super.visitLdcInsn(Type.getType("Ljava/lang/String;"));
                super.visitInsn(AASTORE);
                super.visitInsn(DUP);
                super.visitInsn(ICONST_1);
                super.visitLdcInsn(Type.getType("Ljava/lang/String;"));
                super.visitInsn(AASTORE);
                super.visitInsn(DUP);
                super.visitInsn(ICONST_2);
                super.visitLdcInsn(Type.getType("Ljava/lang/String;"));
                super.visitInsn(AASTORE);
                super.visitInsn(DUP);
                super.visitInsn(ICONST_3);
                super.visitLdcInsn(Type.getType("Ljava/lang/Object;"));
                super.visitInsn(AASTORE);

                super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;");

                super.visitVarInsn(ALOAD, methodCallTraceBuilderLocalVar);

                super.visitInsn(ICONST_4);
                super.visitTypeInsn(ANEWARRAY, "java/lang/Object");
                super.visitInsn(DUP);
                super.visitInsn(ICONST_0);
                super.visitLdcInsn(getClassName());
                super.visitInsn(AASTORE);
                super.visitInsn(DUP);
                super.visitInsn(ICONST_1);
                super.visitLdcInsn(getMethodName());
                super.visitInsn(AASTORE);
                super.visitInsn(DUP);
                super.visitInsn(ICONST_2);
                super.visitLdcInsn(getMethodDescriptor());
                super.visitInsn(AASTORE);
                super.visitInsn(DUP);
                super.visitInsn(ICONST_3);
                super.visitInsn(ACONST_NULL);
                super.visitInsn(AASTORE);


                super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;");
                super.visitInsn(POP);

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    /**
     * @return
     */
    private String getClassName() {
        return this.className;
    }

    /**
     * @return
     */
    private String getMethodName() {
        return this.methodName;
    }

    /**
     * @return
     */
    private String getMethodDescriptor() {
        return super.methodDesc;
    }

    /**
     * @param argIndex
     * @return
     */
    private int generateArgumentsArray(int argIndex) {

        Type[] argumentTypes = Type.getArgumentTypes(getMethodDescriptor());

        super.visitIntInsn(BIPUSH, argumentTypes.length);
        super.visitTypeInsn(ANEWARRAY, "java/lang/Object");

        for (int i = 0; i < argumentTypes.length; i++) {
            Type argumentType = argumentTypes[i];

            super.visitInsn(DUP);
            super.visitIntInsn(BIPUSH, i);
            super.visitVarInsn(argumentType.getOpcode(ILOAD), argIndex);

            // boxing start

            switch (argumentType.getSort()) {
                case Type.BYTE:
                    super.visitMethodInsn(
                            INVOKESTATIC,
                            "java/lang/Byte",
                            "valueOf",
                            "(B)Ljava/lang/Byte;"
                    );
                    break;
                case Type.BOOLEAN:
                    super.visitMethodInsn(
                            INVOKESTATIC,
                            "java/lang/Boolean",
                            "valueOf",
                            "(Z)Ljava/lang/Boolean;"
                    );
                    break;
                case Type.SHORT:
                    super.visitMethodInsn(
                            INVOKESTATIC,
                            "java/lang/Short",
                            "valueOf",
                            "(S)Ljava/lang/Short;"
                    );
                    break;
                case Type.CHAR:
                    super.visitMethodInsn(
                            INVOKESTATIC,
                            "java/lang/Character",
                            "valueOf",
                            "(C)Ljava/lang/Character;"
                    );
                    break;
                case Type.INT:
                    super.visitMethodInsn(
                            INVOKESTATIC,
                            "java/lang/Integer",
                            "valueOf",
                            "(I)Ljava/lang/Integer;"
                    );
                    break;
                case Type.FLOAT:
                    super.visitMethodInsn(
                            INVOKESTATIC,
                            "java/lang/Float",
                            "valueOf",
                            "(F)Ljava/lang/Float;"
                    );
                    break;
                case Type.LONG:
                    super.visitMethodInsn(
                            INVOKESTATIC,
                            "java/lang/Long",
                            "valueOf",
                            "(J)Ljava/lang/Long;"
                    );
                    break;
                case Type.DOUBLE:
                    super.visitMethodInsn(
                            INVOKESTATIC,
                            "java/lang/Double",
                            "valueOf",
                            "(D)Ljava/lang/Double;"
                    );
                    break;
            }

            // boxing end

            super.visitInsn(AASTORE);

            argIndex += argumentType.getSize();
        }

        super.visitVarInsn(ASTORE, argIndex);
        return argIndex;
    }

}
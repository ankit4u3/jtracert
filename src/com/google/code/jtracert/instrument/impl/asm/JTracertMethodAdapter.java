package com.google.code.jtracert.instrument.impl.asm;

import com.google.code.jtracert.config.InstrumentationProperties;
import com.google.code.jtracert.instrument.ConfigurableTransformer;
import com.google.code.jtracert.util.ClassUtils;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.Queue;
import java.util.LinkedList;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class JTracertMethodAdapter extends AdviceAdapter implements ConfigurableTransformer {

    private final String className;
    private String parentClassName;
    private final String methodName;
    private final boolean isConstructor;

    private InstrumentationProperties instrumentationProperties;

    private final Label startFinallyLabel = new Label();

    /**
     * @param mv
     * @param access
     * @param name
     * @param desc
     * @param className
     */
    public JTracertMethodAdapter(MethodVisitor mv, int access, String name, String desc, String className) {
        super(mv, access, name, desc);
        this.className = className;
        this.methodName = name;
        this.isConstructor = name.equals(ClassUtils.CONSTRUCTOR_METHOD_NAME);
    }

    /**
     * @param mv
     * @param access
     * @param name
     * @param desc
     * @param className
     * @param instrumentationProperties
     * @param parentClassName
     */
    public JTracertMethodAdapter(
            MethodVisitor mv,
            int access,
            String name,
            String desc,
            String className,
            InstrumentationProperties instrumentationProperties,
            String parentClassName) {
        this(mv, access, name, desc, className);
        this.instrumentationProperties = instrumentationProperties;
        this.parentClassName = parentClassName;
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
     * @param owner
     * @param name
     * @param desc
     */
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {

        if (isConstructor && name.equals(ClassUtils.CONSTRUCTOR_METHOD_NAME) && (owner.equals(parentClassName) || ClassUtils.getFullyQualifiedName(owner).equals(getClassName()))) {

            /*if ((null != getInstrumentationProperties()) && (getInstrumentationProperties().isVerbose())) {
                System.out.println("Instrumenting constructor " + getClassName() + ".<init>" + getMethodDescriptor());
            }*/

            super.visitMethodInsn(
                    INVOKESTATIC,
                    "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilderFactory",
                    "getMethodCallTraceBuilder",
                    "()Lcom/google/code/jtracert/traceBuilder/MethodCallTraceBuilder;"
            );

            super.visitLdcInsn(getClassName());
            super.visitLdcInsn(getMethodDescriptor());

            super.visitMethodInsn(
                    INVOKEINTERFACE,
                    "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilder",
                    "preEnterConstructor",
                    "(Ljava/lang/String;Ljava/lang/String;)V"
            );

        }

        super.visitMethodInsn(opcode, owner, name, desc);

        /*if (Opcodes.INVOKESPECIAL == opcode) {

            if (!owner.equals(parentClassName) && !ClassUtils.getFullyQualifiedName(owner).equals(getClassName())) {

                System.out.println("Instrumenting constructor " + owner + "." + name + getMethodDescriptor() + " inside " + getClassName() + "." + getMethodName());

                Integer variable = objects.poll();

                if (null != variable) {

                    super.visitMethodInsn(
                            INVOKESTATIC,
                            "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilderFactory",
                            "getMethodCallTraceBuilder",
                            "()Lcom/google/code/jtracert/traceBuilder/MethodCallTraceBuilder;"
                    );

                    super.visitVarInsn(ALOAD, variable);

                    super.visitMethodInsn(
                            INVOKEINTERFACE,
                            "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilder",
                            "newObject",
                            "(Ljava/lang/Object;)V"
                    );

                } else {
                    System.err.println("Variable is null");
                }

            }

        }*/

    }

    /*private Queue<Integer> objects = new LinkedList<Integer>();

    @Override
    public void visitTypeInsn(int i, String s) {
        super.visitTypeInsn(i, s);
        if (Opcodes.NEW == i) {

            mv.visitInsn(Opcodes.DUP);
            int objectVar = newLocal(Type.getType(Object.class));
            super.visitVarInsn(ASTORE, objectVar);
            objects.offer(objectVar);

            System.out.println("Newly created object is stored in local variable");
        }
    }*/

    /**
     * @param opcode
     */
    private void onFinally(int opcode) {

        if (isConstructor) {

            if (opcode == ATHROW) {

                super.visitInsn(DUP);

                int exceptionVar = newLocal(Type.getType(Throwable.class));
                super.visitVarInsn(ASTORE, exceptionVar);

                super.visitMethodInsn(
                        INVOKESTATIC,
                        "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilderFactory",
                        "getMethodCallTraceBuilder",
                        "()Lcom/google/code/jtracert/traceBuilder/MethodCallTraceBuilder;"
                );

                super.visitLdcInsn(getClassName());
                super.visitLdcInsn(getMethodName());
                super.visitLdcInsn(getMethodDescriptor());
                super.visitVarInsn(ALOAD, exceptionVar);

                super.visitMethodInsn(
                        INVOKEINTERFACE,
                        "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilder",
                        "leaveConstructor",
                        "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V"
                );

            } else {

                mv.visitMethodInsn(
                        INVOKESTATIC,
                        "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilderFactory",
                        "getMethodCallTraceBuilder",
                        "()Lcom/google/code/jtracert/traceBuilder/MethodCallTraceBuilder;"
                );

                mv.visitLdcInsn(getMethodDescriptor());

                mv.visitMethodInsn(
                        INVOKEINTERFACE,
                        "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilder",
                        "leaveConstructor",
                        "(Ljava/lang/String;)V"
                );

            }

        } else {

            if (opcode == ATHROW) {

                super.visitInsn(DUP);

                int exceptionVar = newLocal(Type.getType(Throwable.class));
                super.visitVarInsn(ASTORE, exceptionVar);

                super.visitMethodInsn(
                        INVOKESTATIC,
                        "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilderFactory",
                        "getMethodCallTraceBuilder",
                        "()Lcom/google/code/jtracert/traceBuilder/MethodCallTraceBuilder;"
                );

                super.visitVarInsn(ALOAD, exceptionVar);

                super.visitMethodInsn(
                        INVOKEINTERFACE,
                        "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilder",
                        "exception",
                        "(Ljava/lang/Throwable;)V"
                );

            } else if (opcode == RETURN) {

                super.visitMethodInsn(
                        INVOKESTATIC,
                        "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilderFactory",
                        "getMethodCallTraceBuilder",
                        "()Lcom/google/code/jtracert/traceBuilder/MethodCallTraceBuilder;"
                );

                super.visitMethodInsn(
                        INVOKEINTERFACE,
                        "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilder",
                        "leave",
                        "()V"
                );

            } else {

                if (opcode == LRETURN || opcode == DRETURN) {
                    super.visitInsn(DUP2);
                } else {
                    super.visitInsn(DUP);
                }
                box(Type.getReturnType(this.methodDesc));

                int returnVar = newLocal(Type.getType(Throwable.class));
                super.visitVarInsn(ASTORE, returnVar);

                super.visitMethodInsn(
                        INVOKESTATIC,
                        "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilderFactory",
                        "getMethodCallTraceBuilder",
                        "()Lcom/google/code/jtracert/traceBuilder/MethodCallTraceBuilder;"
                );

                super.visitVarInsn(ALOAD, returnVar);

                super.visitMethodInsn(
                        INVOKEINTERFACE,
                        "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilder",
                        "leave",
                        "(Ljava/lang/Object;)V"
                );

            }


        }

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
     *
     */
    @Override
    protected void onMethodEnter() {
        try {

            if ((ACC_STATIC & methodAccess) == 0) {

                int argIndex = generateArgumentsArray(1);

                super.visitMethodInsn(
                        INVOKESTATIC,
                        "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilderFactory",
                        "getMethodCallTraceBuilder",
                        "()Lcom/google/code/jtracert/traceBuilder/MethodCallTraceBuilder;"
                );

                super.visitLdcInsn(getClassName());
                super.visitLdcInsn(getMethodName());
                super.visitLdcInsn(getMethodDescriptor());
                super.visitVarInsn(ALOAD, 0);
                super.visitVarInsn(ALOAD, argIndex);

                super.visitMethodInsn(
                        INVOKEINTERFACE,
                        "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilder",
                        "enter",
                        "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;[Ljava/lang/Object;)V"
                );

            } else {

                int argIndex = generateArgumentsArray(0);

                super.visitMethodInsn(
                        INVOKESTATIC,
                        "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilderFactory",
                        "getMethodCallTraceBuilder",
                        "()Lcom/google/code/jtracert/traceBuilder/MethodCallTraceBuilder;"
                );

                super.visitLdcInsn(getClassName());
                super.visitLdcInsn(getMethodName());
                super.visitLdcInsn(getMethodDescriptor());
                super.visitInsn(ACONST_NULL);
                super.visitVarInsn(ALOAD, argIndex);

                super.visitMethodInsn(
                        INVOKEINTERFACE,
                        "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilder",
                        "enter",
                        "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;[Ljava/lang/Object;)V"
                );
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

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
     * @return
     */
    private int getMethodAccess() {
        return super.methodAccess;
    }

    /**
     * @return
     */
    public InstrumentationProperties getInstrumentationProperties() {
        return instrumentationProperties;
    }

    /**
     * @param instrumentationProperties
     */
    public void setInstrumentationProperties(InstrumentationProperties instrumentationProperties) {
        this.instrumentationProperties = instrumentationProperties;
    }

}

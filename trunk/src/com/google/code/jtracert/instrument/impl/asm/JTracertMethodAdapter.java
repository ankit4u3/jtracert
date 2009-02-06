package com.google.code.jtracert.instrument.impl.asm;

import com.google.code.jtracert.config.InstrumentationProperties;
import com.google.code.jtracert.instrument.ConfigurableTransformer;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * @author Dmitry Bedrin
 */
public class JTracertMethodAdapter extends AdviceAdapter implements ConfigurableTransformer {

    private final String className;
    private final String methodName;
    private final boolean isConstructor;

    private InstrumentationProperties instrumentationProperties;

    private Label startFinallyLabel = new Label();

    public JTracertMethodAdapter(MethodVisitor mv, int access, String name, String desc, String className) {
        super(mv, access, name, desc);
        this.className = className;
        this.methodName = name;
        this.isConstructor = name.equals("<init>");
    }

    public JTracertMethodAdapter(MethodVisitor mv, int access, String name, String desc, String className, InstrumentationProperties instrumentationProperties) {
        this(mv, access, name, desc, className);
        this.instrumentationProperties = instrumentationProperties;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        super.visitLabel(startFinallyLabel);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        Label endFinallyLabel = new Label();
        super.visitTryCatchBlock(startFinallyLabel, endFinallyLabel, endFinallyLabel, null);
        super.visitLabel(endFinallyLabel);
        onFinally(ATHROW);
        super.visitInsn(ATHROW);
        super.visitMaxs(maxStack, maxLocals);
    }

    @Override
    protected void onMethodExit(int opcode) {
        if (opcode != ATHROW) {
            onFinally(opcode);
        }
    }

    private void onFinally(int opcode) {
        if (opcode == ATHROW) {

            super.visitInsn(DUP);

            int exceptionVar = newLocal(Type.getType(Throwable.class));
            super.visitVarInsn(ASTORE,exceptionVar);

            super.visitMethodInsn(
                    INVOKESTATIC,
                    "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilderFactory",
                    "getMethodCallTraceBuilder",
                    "()Lcom/google/code/jtracert/traceBuilder/MethodCallTraceBuilder;"
            );

            super.visitVarInsn(ALOAD,exceptionVar);

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

            if(opcode==LRETURN || opcode==DRETURN) {
                super.visitInsn(DUP2);
            } else {
                super.visitInsn(DUP);
            }
            box(Type.getReturnType(this.methodDesc));

            int returnVar = newLocal(Type.getType(Throwable.class));
            super.visitVarInsn(ASTORE,returnVar);

            super.visitMethodInsn(
                    INVOKESTATIC,
                    "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilderFactory",
                    "getMethodCallTraceBuilder",
                    "()Lcom/google/code/jtracert/traceBuilder/MethodCallTraceBuilder;"
            );

            super.visitVarInsn(ALOAD,returnVar);

            super.visitMethodInsn(
                    INVOKEINTERFACE,
                    "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilder",
                    "leave",
                    "(Ljava/lang/Object;)V"
            );

        }

    }

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

    private String getClassName() {
        return this.className;
    }

    private String getMethodName() {
        return this.methodName;
    }

    private String getMethodDescriptor() {
        return super.methodDesc;
    }

    private int getMethodAccess() {
        return super.methodAccess;
    }

    public InstrumentationProperties getInstrumentationProperties() {
        return instrumentationProperties;
    }

    public void setInstrumentationProperties(InstrumentationProperties instrumentationProperties) {
        this.instrumentationProperties = instrumentationProperties;
    }

}

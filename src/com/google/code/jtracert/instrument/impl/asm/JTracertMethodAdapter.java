package com.google.code.jtracert.instrument.impl.asm;

import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import com.google.code.jtracert.instrument.ConfigurableTransformer;
import com.google.code.jtracert.config.InstrumentationProperties;

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
        mv.visitLabel(startFinallyLabel);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        Label endFinallyLabel = new Label();
        mv.visitTryCatchBlock(startFinallyLabel, endFinallyLabel, endFinallyLabel, null);
        mv.visitLabel(endFinallyLabel);
        onFinally(ATHROW);
        mv.visitInsn(ATHROW);
        mv.visitMaxs(maxStack, maxLocals);
    }

    @Override
    protected void onMethodExit(int opcode) {
        if (opcode != ATHROW) {
            onFinally(opcode);
        }
    }

    private void onFinally(int opcode) {
        if (opcode == ATHROW) {

            mv.visitInsn(DUP);

            int exceptionVar = newLocal(Type.getType(Throwable.class));
            mv.visitVarInsn(ASTORE,exceptionVar);

            mv.visitMethodInsn(
                    INVOKESTATIC,
                    "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilderFactory",
                    "getMethodCallTraceBuilder",
                    "()Lcom/google/code/jtracert/traceBuilder/MethodCallTraceBuilder;"
            );

            mv.visitVarInsn(ALOAD,exceptionVar);

            mv.visitMethodInsn(
                    INVOKEINTERFACE,
                    "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilder",
                    "exception",
                    "(Ljava/lang/Throwable;)V"
            );

        } else if (opcode == RETURN) {

            mv.visitMethodInsn(
                    INVOKESTATIC,
                    "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilderFactory",
                    "getMethodCallTraceBuilder",
                    "()Lru/bedrin/traceBuilder/MethodCallTraceBuilder;"
            );

            mv.visitMethodInsn(
                    INVOKEINTERFACE,
                    "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilder",
                    "leave",
                    "()V"
            );

        } else {

            if(opcode==LRETURN || opcode==DRETURN) {
                mv.visitInsn(DUP2);
            } else {
                mv.visitInsn(DUP);
            }
            box(Type.getReturnType(this.methodDesc));

            int returnVar = newLocal(Type.getType(Throwable.class));
            mv.visitVarInsn(ASTORE,returnVar);

            mv.visitMethodInsn(
                    INVOKESTATIC,
                    "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilderFactory",
                    "getMethodCallTraceBuilder",
                    "()Lcom/google/code/jtracert/traceBuilder/MethodCallTraceBuilder;"
            );

            mv.visitVarInsn(ALOAD,returnVar);

            mv.visitMethodInsn(
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

                if (isConstructor) {
                    super.visitVarInsn(ALOAD, 0);
                    super.visitTypeInsn(NEW,"com/google/code/jtracert/model/JTracertObjectCompanion");
                    super.visitInsn(DUP);
                    super.visitMethodInsn(
                            INVOKESPECIAL,
                            "com/google/code/jtracert/model/JTracertObjectCompanion",
                            "<init>",
                            "()V"
                    );
                    super.visitFieldInsn(
                            PUTFIELD,
                            className.replace('.','/'),
                            "jTracertObjectCompanion",
                            "Lcom/google/code/jtracert/model/JTracertObjectCompanion;"
                    );
                }


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

                super.visitVarInsn(ALOAD, 0);
                super.visitFieldInsn(
                        GETFIELD,
                        className.replace('.','/'),
                        "jTracertObjectCompanion",
                        "Lcom/google/code/jtracert/model/JTracertObjectCompanion;"
                );

                super.visitMethodInsn(
                        INVOKEINTERFACE,
                        "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilder",
                        "enter",
                        "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;[Ljava/lang/Object;Lru/bedrin/model/JTracertObjectCompanion;)V"
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
                super.visitInsn(ACONST_NULL);

                super.visitMethodInsn(
                        INVOKEINTERFACE,
                        "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilder",
                        "enter",
                        "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;[Ljava/lang/Object;Lru/bedrin/model/JTracertObjectCompanion;)V"
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

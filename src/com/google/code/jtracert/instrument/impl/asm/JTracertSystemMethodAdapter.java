package com.google.code.jtracert.instrument.impl.asm;

import com.google.code.jtracert.config.InstrumentationProperties;
import com.google.code.jtracert.instrument.ConfigurableTransformer;
import com.google.code.jtracert.util.ClassUtils;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class JTracertSystemMethodAdapter extends MethodAdapter implements Opcodes {

    public JTracertSystemMethodAdapter(MethodVisitor methodVisitor) {
        super(methodVisitor);
    }

    @Override
    public void visitCode() {
        super.visitCode();

        mv.visitVarInsn(ALOAD,0);
        mv.visitMethodInsn(
                INVOKESTATIC,
                "com/google/code/jtracert/traceBuilder/MethodCallTraceBuilderFactory",
                "constructor",
                "(Ljava/lang/Object;)V"
        );

        mv.visitInsn(RETURN);
    }
    
}

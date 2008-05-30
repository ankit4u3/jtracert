package com.google.code.jtracert.instrument.impl.asm;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldNode;
import com.google.code.jtracert.util.ClassUtils;
import com.google.code.jtracert.instrument.ConfigurableTransformer;
import com.google.code.jtracert.config.InstrumentationProperties;

/**
 * @author Dmitry Bedrin
 */
public class JTracertClassAdapter extends ClassAdapter implements ConfigurableTransformer {

    private String className;
    private InstrumentationProperties instrumentationProperties;

    private FieldNode jTracertObjectCompanion;
    private boolean isInterface;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public InstrumentationProperties getInstrumentationProperties() {
        return instrumentationProperties;
    }

    public void setInstrumentationProperties(InstrumentationProperties instrumentationProperties) {
        this.instrumentationProperties = instrumentationProperties;
    }

    public JTracertClassAdapter(ClassVisitor visitor) {
        super(visitor);
    }

    public JTracertClassAdapter(ClassVisitor visitor, InstrumentationProperties instrumentationProperties) {
        super(visitor);
        this.instrumentationProperties = instrumentationProperties;
    }

    @Override
    public void visit(int version,
                  int access,
                  String name,
                  String signature,
                  String superName,
                  String[] interfaces) {
        if (0 == (access & Opcodes.ACC_INTERFACE)) {
            this.jTracertObjectCompanion = new FieldNode(
                    Opcodes.ACC_PUBLIC,
                    "jTracertObjectCompanion",
                    "Lcom/google/code/jtracert/model/JTracertObjectCompanion;",
                    null,
                    null);
            isInterface = false;
        } else {
            isInterface = true;
        }
        setClassName(ClassUtils.getFullyQualifiedName(name));
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

        MethodVisitor parentMethodVisitor = super.visitMethod(
                access,
                name,
                desc,
                signature,
                exceptions
        );

        MethodVisitor jTracertMethodAdapter = new JTracertMethodAdapter(
                parentMethodVisitor,
                access,
                name,
                desc,
                getClassName(),
                getInstrumentationProperties()
        );

        return jTracertMethodAdapter;

    }

    @Override
    public void visitEnd() {
        if (!isInterface) {
            jTracertObjectCompanion.accept(cv);
        }
        super.visitEnd();
    }

}

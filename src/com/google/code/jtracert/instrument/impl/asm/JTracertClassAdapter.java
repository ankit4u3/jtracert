package com.google.code.jtracert.instrument.impl.asm;

import com.google.code.jtracert.config.InstrumentationProperties;
import com.google.code.jtracert.instrument.ConfigurableTransformer;
import com.google.code.jtracert.util.ClassUtils;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class JTracertClassAdapter extends ClassAdapter implements ConfigurableTransformer {

    private String className;
    private String parentClassName;
    private InstrumentationProperties instrumentationProperties;

    private boolean isInterface;

    /**
     * @return
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return
     */
    public String getParentClassName() {
        return parentClassName;
    }

    /**
     * @param parentClassName
     */
    public void setParentClassName(String parentClassName) {
        this.parentClassName = parentClassName;
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

    /**
     * @param visitor
     */
    public JTracertClassAdapter(ClassVisitor visitor) {
        super(visitor);
    }

    /**
     * @param visitor
     * @param instrumentationProperties
     */
    public JTracertClassAdapter(ClassVisitor visitor, InstrumentationProperties instrumentationProperties) {
        super(visitor);
        this.instrumentationProperties = instrumentationProperties;
    }

    /**
     * @param version
     * @param access
     * @param name
     * @param signature
     * @param superName
     * @param interfaces
     */
    @Override
    public void visit(int version,
                      int access,
                      String name,
                      String signature,
                      String superName,
                      String[] interfaces) {

        isInterface = 0 != (access & Opcodes.ACC_INTERFACE);

        setClassName(ClassUtils.getFullyQualifiedName(name));
        setParentClassName(superName);

        super.visit(version, access, name, signature, superName, interfaces);

    }

    /**
     * @param access
     * @param name
     * @param desc
     * @param signature
     * @param exceptions
     * @return
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

        if (0 == (access & Opcodes.ACC_SYNTHETIC)) {

            MethodVisitor parentMethodVisitor = super.visitMethod(
                    access,
                    name,
                    desc,
                    signature,
                    exceptions
            );

            return new JTracertMethodAdapter(
                    parentMethodVisitor,
                    access,
                    name,
                    desc,
                    getClassName(),
                    getInstrumentationProperties(),
                    getParentClassName()
            );

        } else {

            if ((null != getInstrumentationProperties()) && (getInstrumentationProperties().isVerbose())) {
                System.out.println("Skiping syntetic method " + name);
            }

            return super.visitMethod(
                    access,
                    name,
                    desc,
                    signature,
                    exceptions
            );

        }

    }

    /**
     *
     */
    @Override
    public void visitEnd() {

        if (!isInterface) {
            // Apply class transformations
        }

        super.visitEnd();
    }

}

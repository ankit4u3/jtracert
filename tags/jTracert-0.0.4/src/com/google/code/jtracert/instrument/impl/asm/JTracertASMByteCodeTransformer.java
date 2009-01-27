package com.google.code.jtracert.instrument.impl.asm;

import com.google.code.jtracert.instrument.ByteCodeTransformException;
import com.google.code.jtracert.instrument.impl.BaseJTracertByteCodeTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

/**
 * @author Dmitry Bedrin
 */
public class JTracertASMByteCodeTransformer extends BaseJTracertByteCodeTransformer {

    @Override
    public byte[] transform(byte[] originalBytes, int offset, int length) throws ByteCodeTransformException {

        try {

            ClassReader classReader = new ClassReader(originalBytes, offset, length);

            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);

            ClassVisitor classVisitor = new JTracertClassAdapter(classWriter);

            classReader.accept(classVisitor,0);

            return classWriter.toByteArray();

        } catch (Throwable e) {
            throw new ByteCodeTransformException(e);
        }

    }

}

package com.google.code.jtracert.instrument.impl.asm;

import com.google.code.jtracert.instrument.ByteCodeTransformException;
import com.google.code.jtracert.instrument.impl.BaseJTracertByteCodeTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.ASMifierClassVisitor;

import java.io.PrintWriter;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class JTracertASMByteCodeTransformer extends BaseJTracertByteCodeTransformer {

    /**
     * @param originalBytes
     * @param offset
     * @param length
     * @return
     * @throws ByteCodeTransformException
     */
    @Override
    public byte[] transform(byte[] originalBytes, int offset, int length) throws ByteCodeTransformException {

        try {

            ClassReader classReader = new ClassReader(originalBytes, offset, length);

            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);

            ClassVisitor classVisitor = new JTracertClassAdapter(classWriter);

            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);

            byte[] transformedBytes = classWriter.toByteArray();

            //dumpBytes(transformedBytes);

            return transformedBytes;

        } catch (Throwable e) {
            throw new ByteCodeTransformException(e);
        }

    }

    /**
     * @param bytes
     */
    @Deprecated
    private static void dumpBytes(byte[] bytes) {

        try {
            ClassReader classReader = new ClassReader(bytes);

            ClassVisitor classVisitor = new ASMifierClassVisitor(new PrintWriter(System.out));

            classReader.accept(classVisitor, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

package com.google.code.jtracert.instrument;

/**
 * @author Dmitry Bedrin
 */
public interface ByteCodeTransformer {

    byte[] transform(byte[] originalBytes, int offset, int length) throws ByteCodeTransformException;

}

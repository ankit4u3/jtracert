package com.google.code.jtracert.instrument;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 * @author Dmitry.Bedrin@gmail.com
 */
public interface ByteCodeTransformer {

    /**
     *
     * @param originalBytes
     * @param offset
     * @param length
     * @return
     * @throws ByteCodeTransformException
     */
    byte[] transform(byte[] originalBytes, int offset, int length) throws ByteCodeTransformException;

}

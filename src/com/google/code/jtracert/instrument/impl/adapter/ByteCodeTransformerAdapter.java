package com.google.code.jtracert.instrument.impl.adapter;

import com.google.code.jtracert.instrument.ByteCodeTransformException;
import com.google.code.jtracert.instrument.ByteCodeTransformer;

import java.io.*;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class ByteCodeTransformerAdapter implements ByteCodeTransformer {

    private ByteCodeTransformer byteCodeTransformer;

    /**
     * @return
     */
    public ByteCodeTransformer getByteCodeTransformer() {
        return byteCodeTransformer;
    }

    /**
     * @param byteCodeTransformer
     */
    public void setByteCodeTransformer(ByteCodeTransformer byteCodeTransformer) {
        this.byteCodeTransformer = byteCodeTransformer;
    }

    /**
     * @param byteCodeTransformer
     */
    public ByteCodeTransformerAdapter(ByteCodeTransformer byteCodeTransformer) {
        super();
        setByteCodeTransformer(byteCodeTransformer);
    }

    /**
     * @param originalBytes
     * @param offset
     * @param length
     * @return
     * @throws ByteCodeTransformException
     */
    public byte[] transform(byte[] originalBytes, int offset, int length, boolean instrumentClass) throws ByteCodeTransformException {

        assert null != getByteCodeTransformer();

        return getByteCodeTransformer().transform(originalBytes, offset, length, instrumentClass);

    }

    /**
     * @param originalBytes
     * @return
     * @throws ByteCodeTransformException
     */
    public byte[] transform(byte[] originalBytes, boolean instrumentClass) throws ByteCodeTransformException {

        assert null != originalBytes;

        return transform(originalBytes, 0, originalBytes.length, instrumentClass);

    }

    /**
     * @param inputStream
     * @param outputStream
     * @throws ByteCodeTransformException
     */
    public void transform(InputStream inputStream, OutputStream outputStream, boolean instrumentClass) throws ByteCodeTransformException {

        assert null != inputStream;
        assert null != outputStream;

        byte[] transformedByteArray = transform(inputStream, instrumentClass);

        if (null != transformedByteArray) {

            ByteArrayInputStream transformedByteArrayInputStream = new ByteArrayInputStream(transformedByteArray);

            try {
                while (transformedByteArrayInputStream.available() > 0) {
                    outputStream.write(transformedByteArrayInputStream.read());
                }
            } catch (IOException e) {
                throw new ByteCodeTransformException(e);
            } finally {
                try {
                    transformedByteArrayInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace(); // todo refactor this line
                }
            }

        }

    }

    /**
     * @param inputStream
     * @return
     * @throws ByteCodeTransformException
     */
    public byte[] transform(InputStream inputStream, boolean instrumentClass) throws ByteCodeTransformException {

        assert null != inputStream;

        // Read byte array from input stream and transform it

        ByteArrayOutputStream originalByteArrayOutputStream = new ByteArrayOutputStream();

        try {

            while (inputStream.available() > 0) {
                originalByteArrayOutputStream.write(inputStream.read());
            }

            return transform(originalByteArrayOutputStream.toByteArray(), instrumentClass);

        } catch (IOException e) {
            throw new ByteCodeTransformException(e);
        } finally {
            try {
                originalByteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace(); // todo refactor this line
            }
        }

    }

}

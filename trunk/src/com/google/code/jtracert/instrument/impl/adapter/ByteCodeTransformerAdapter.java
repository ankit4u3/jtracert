package com.google.code.jtracert.instrument.impl.adapter;

import com.google.code.jtracert.instrument.ByteCodeTransformException;
import com.google.code.jtracert.instrument.ByteCodeTransformer;

import java.io.*;

/**
 * @author Dmitry Bedrin
 */
public class ByteCodeTransformerAdapter implements ByteCodeTransformer {

    private ByteCodeTransformer byteCodeTransformer;

    public ByteCodeTransformer getByteCodeTransformer() {
        return byteCodeTransformer;
    }

    public void setByteCodeTransformer(ByteCodeTransformer byteCodeTransformer) {
        this.byteCodeTransformer = byteCodeTransformer;
    }

    public ByteCodeTransformerAdapter(ByteCodeTransformer byteCodeTransformer) {
        super();
        setByteCodeTransformer(byteCodeTransformer);
    }

    public byte[] transform(byte[] originalBytes, int offset, int length) throws ByteCodeTransformException {

        assert null != getByteCodeTransformer();

        return getByteCodeTransformer().transform(originalBytes, offset, length);

    }

    public byte[] transform(byte[] originalBytes) throws ByteCodeTransformException {

        assert null != originalBytes;

        return transform(originalBytes, 0, originalBytes.length);

    }

    public void transform(InputStream inputStream, OutputStream outputStream) throws ByteCodeTransformException {

        assert null != inputStream;
        assert null != outputStream;

        // Read byte array from input stream and transform it

        byte[] transformedByteArray;

        ByteArrayOutputStream originalByteArrayOutputStream = new ByteArrayOutputStream();

        try {

            while (inputStream.available() > 0) {
                originalByteArrayOutputStream.write(inputStream.read());
            }

            transformedByteArray = transform(originalByteArrayOutputStream.toByteArray());

        } catch (IOException e) {
            throw new ByteCodeTransformException(e);
        } finally {
            try {
                originalByteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace(); // todo refactor this line
            }
        }

        // Write transformed byte array to given output stream

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

}

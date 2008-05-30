package com.google.code.jtracert.instrument;

/**
 * @author Dmitry Bedrin
 */
public class ByteCodeTransformException extends Exception {

    public ByteCodeTransformException() {
        super();
    }

    public ByteCodeTransformException(String message) {
        super(message);
    }

    public ByteCodeTransformException(String message, Throwable cause) {
        super(message, cause);
    }

    public ByteCodeTransformException(Throwable cause) {
        super(cause);
    }

}

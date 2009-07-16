package com.google.code.jtracert.instrument;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class ByteCodeTransformException extends Exception {

    /**
     *
     */
    public ByteCodeTransformException() {
        super();
    }

    /**
     * @param message
     */
    public ByteCodeTransformException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public ByteCodeTransformException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public ByteCodeTransformException(Throwable cause) {
        super(cause);
    }

}

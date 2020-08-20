package io.andy.pigeon.net.core.exception;

public class SerializationException extends Exception {

    /**
     * Constructor.
     */
    public SerializationException() {

    }

    /**
     * Constructor.
     */
    public SerializationException(String message) {
        super(message);
    }

    /**
     * Constructor.
     */
    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}

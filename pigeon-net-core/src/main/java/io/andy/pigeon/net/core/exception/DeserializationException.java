package io.andy.pigeon.net.core.exception;

public class DeserializationException extends Exception {

    /**
     * Constructor.
     */
    public DeserializationException() {

    }

    /**
     * Constructor.
     */
    public DeserializationException(String message) {
        super(message);
    }

    /**
     * Constructor.
     */
    public DeserializationException(String message, Throwable cause) {
        super(message, cause);
    }
}

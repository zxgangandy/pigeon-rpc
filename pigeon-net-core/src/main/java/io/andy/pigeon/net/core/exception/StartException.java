
package io.andy.pigeon.net.core.exception;


public class StartException extends RuntimeException {

    public StartException(String message, Throwable cause) {
        super(message, cause);
    }

    public StartException(String message) {
        super(message);
    }
}

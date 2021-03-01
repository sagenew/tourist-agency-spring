package com.touristagency.util.exception;

/**
 * Runtime exception that is thrown if new username already exists in the database
 */
public class UsernameNotUniqueException extends RuntimeException {
    public UsernameNotUniqueException() {
    }

    public UsernameNotUniqueException(String message) {
        super(message);
    }

    public UsernameNotUniqueException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameNotUniqueException(Throwable cause) {
        super(cause);
    }

    public UsernameNotUniqueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

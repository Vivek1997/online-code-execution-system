package com.vivek.onlinecodeexecutionsystem.exceptions;

public class NoSuchLanguageException extends RuntimeException {
    public NoSuchLanguageException() {
    }

    public NoSuchLanguageException(String message) {
        super(message);
    }

    public NoSuchLanguageException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchLanguageException(Throwable cause) {
        super(cause);
    }

    public NoSuchLanguageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

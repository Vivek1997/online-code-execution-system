package com.vivek.onlinecodeexecutionsystem.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class InvalidSubmissionException extends RuntimeException {
    public InvalidSubmissionException() {
    }

    public InvalidSubmissionException(String message) {
        super(message);
    }

    public InvalidSubmissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSubmissionException(Throwable cause) {
        super(cause);
    }

    public InvalidSubmissionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

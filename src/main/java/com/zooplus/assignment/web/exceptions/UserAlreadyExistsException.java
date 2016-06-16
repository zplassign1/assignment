package com.zooplus.assignment.web.exceptions;

/**
 * Created by richard on 6/13/16.
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super();
    }

    public UserAlreadyExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExistsException(final String message) {
        super(message);
    }

    public UserAlreadyExistsException(final Throwable cause) {
        super(cause);
    }

}

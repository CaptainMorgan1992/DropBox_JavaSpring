package me.code.individual_assignment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class to represent an exception when access to a path is forbidden.
 * It extends the RuntimeException class.
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenPathException extends RuntimeException {

    /**
     * Constructs a ForbiddenPathException with the specified error message.
     *
     * @param message The error message for the exception.
     */
    public ForbiddenPathException(String message) {
        super(message);
    }
}

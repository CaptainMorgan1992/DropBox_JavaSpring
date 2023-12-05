package me.code.individual_assignment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class to represent an exception when an invalid token is encountered.
 * It extends the RuntimeException class.
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InvalidTokenException extends RuntimeException {

    /**
     * Constructs an InvalidTokenException with the specified error message.
     *
     * @param message The error message for the exception.
     */
    public InvalidTokenException(String message) {
        super(message);
    }
}

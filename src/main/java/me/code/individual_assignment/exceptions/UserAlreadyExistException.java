package me.code.individual_assignment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class to represent an exception when attempting to create a user that already exists.
 * It extends the RuntimeException class.
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserAlreadyExistException extends RuntimeException {

    /**
     * Constructs a UserAlreadyExistException with the specified error message.
     *
     * @param message The error message for the exception.
     */
    public UserAlreadyExistException(String message) {
        super(message);
    }
}

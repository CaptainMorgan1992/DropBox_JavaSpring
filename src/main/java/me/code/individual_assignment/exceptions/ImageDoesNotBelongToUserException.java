package me.code.individual_assignment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class to represent an exception when an image does not belong to the user.
 * It extends the RuntimeException class.
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ImageDoesNotBelongToUserException extends RuntimeException {

    /**
     * Constructs an ImageDoesNotBelongToUserException with the specified error message.
     *
     * @param message The error message for the exception.
     */
    public ImageDoesNotBelongToUserException(String message) {
        super(message);
    }
}

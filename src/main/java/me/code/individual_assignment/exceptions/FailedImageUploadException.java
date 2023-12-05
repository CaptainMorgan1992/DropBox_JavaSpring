package me.code.individual_assignment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class to represent an exception when an image upload fails.
 * It extends the RuntimeException class.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FailedImageUploadException extends RuntimeException {

    /**
     * Constructs a FailedImageUploadException with the specified error message.
     *
     * @param message The error message for the exception.
     */
    public FailedImageUploadException(String message) {
        super(message);
    }
}

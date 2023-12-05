package me.code.individual_assignment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class to represent an exception when the size of an image exceeds the allowed limit.
 * It extends the RuntimeException class.
 */
@ResponseStatus(value = HttpStatus.PAYLOAD_TOO_LARGE)
public class ImageSizeTooLargeException extends RuntimeException {

    /**
     * Constructs an ImageSizeTooLargeException with the specified error message.
     *
     * @param message The error message for the exception.
     */
    public ImageSizeTooLargeException(String message) {
        super(message);
    }
}

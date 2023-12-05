package me.code.individual_assignment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Custom exception class to represent an exception related to byte conversion.
 * It extends the RuntimeException class.
 */
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class ByteConversionException extends RuntimeException {

    /**
     * Constructs a ByteConversionException with the specified error message.
     *
     * @param message The error message for the exception.
     */
    public ByteConversionException(String message) {
        super(message);
    }
}

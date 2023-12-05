package me.code.individual_assignment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class to represent an exception when a folder is not found.
 * It extends the RuntimeException class.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FolderNotFoundException extends RuntimeException {

    /**
     * Constructs a FolderNotFoundException with the specified error message.
     *
     * @param message The error message for the exception.
     */
    public FolderNotFoundException(String message) {
        super(message);
    }
}

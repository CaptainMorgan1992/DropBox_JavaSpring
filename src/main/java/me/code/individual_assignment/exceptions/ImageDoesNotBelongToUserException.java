package me.code.individual_assignment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ImageDoesNotBelongToUserException extends RuntimeException{
    public ImageDoesNotBelongToUserException(String message) {
        super(message);
    }
}

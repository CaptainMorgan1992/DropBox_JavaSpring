package com.example.me.code.individual_assignment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FolderNotFoundException extends RuntimeException{
    public FolderNotFoundException(String message) {
        super(message);
    }
}

package com.example.me.code.individual_assignment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class ByteConversionException extends RuntimeException{
    public ByteConversionException(String message) {
        super(message);
    }
}

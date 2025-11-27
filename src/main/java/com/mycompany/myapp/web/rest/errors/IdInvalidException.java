package com.mycompany.myapp.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IdInvalidException extends RuntimeException {

    public IdInvalidException(String message) {
        super(message);
    }
}

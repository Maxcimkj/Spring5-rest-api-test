package com.mekh.spring.university.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class NotFoundException extends Exception {
    public NotFoundException(String message) {
        super(message);
    }
}

package com.mekh.spring.university.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SystemErrorException extends Exception {
    public SystemErrorException(Throwable cause) {
        super(cause);
    }
}

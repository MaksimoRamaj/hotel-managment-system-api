package com.example.hotelManagmentSystem.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class UploadImageException extends RuntimeException {
    public UploadImageException(String message) {
        super(message);
    }
}


package com.example.hotelManagmentSystem.core.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class BookingException extends RuntimeException{
    public BookingException(String message) {
        super(message);
    }
}

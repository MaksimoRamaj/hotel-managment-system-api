package com.example.hotelManagmentSystem.core.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Builder
public class ResourceException {
    private final String message;
    private final Throwable throwable;
    private final HttpStatus httpStatus;
}

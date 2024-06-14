package com.example.hotelManagmentSystem.core.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
public class InputFormatException {
    private final String message;
}

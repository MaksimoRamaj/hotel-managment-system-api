package com.example.hotelManagmentSystem.dataproviders.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    @NotBlank(message = "email must not be blank!")
    private String email;
    @NotBlank(message = "password must not be blank!")
    private String password;
}
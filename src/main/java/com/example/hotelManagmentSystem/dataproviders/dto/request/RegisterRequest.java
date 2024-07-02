package com.example.hotelManagmentSystem.dataproviders.dto.request;

import com.example.hotelManagmentSystem.dataproviders.entity.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Username blank!")
    private String username;
    @NotBlank(message = "Firstname blank!")
    private String firstname;
    @NotBlank(message = "Lastname blank!")
    private String lastname;
    @NotBlank(message = "Email blank!")
    private String email;
    @NotBlank(message = "Password blank!")
    private String password;
    @NotBlank(message = "Role blank!")
    private String role;
    @Valid
    private Address address;
}


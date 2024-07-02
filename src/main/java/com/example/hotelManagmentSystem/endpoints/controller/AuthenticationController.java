package com.example.hotelManagmentSystem.endpoints.controller;

import com.example.hotelManagmentSystem.dataproviders.dto.request.AuthenticationRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.request.RegisterRequest;
import com.example.hotelManagmentSystem.dataproviders.service.interfaces.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final IUserService iUserService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
          @Valid @RequestBody RegisterRequest request
    ){
        return iUserService.register(request);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
          @Valid @RequestBody AuthenticationRequest request
    ){
        return iUserService.authenticate(request);
    }
}

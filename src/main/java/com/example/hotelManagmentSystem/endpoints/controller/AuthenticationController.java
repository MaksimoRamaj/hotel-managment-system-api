package com.example.hotelManagmentSystem.endpoints.controller;

import com.example.hotelManagmentSystem.dataproviders.dto.request.AuthenticationRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.request.RegisterRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.response.AuthenticationResponse;
import com.example.hotelManagmentSystem.dataproviders.service.interfaces.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/expired-token")
    public ResponseEntity<?> authenticate(
    ){
        return new ResponseEntity<>("\"message\" : \"Token revoked or expired!\"" , HttpStatusCode.valueOf(403));
    }
}

package com.example.hotelManagmentSystem.dataproviders.service.interfaces;

import com.example.hotelManagmentSystem.dataproviders.dto.request.AuthenticationRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.request.RegisterRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.response.AuthenticationResponse;
import com.example.hotelManagmentSystem.dataproviders.entity.Role;
import com.example.hotelManagmentSystem.dataproviders.entity.User;
import org.springframework.http.ResponseEntity;

public interface IUserService {
    ResponseEntity<?> authenticate(AuthenticationRequest authenticationRequest);
    ResponseEntity<?> register (RegisterRequest registerRequest);
    Role saveRole(Role role);
    User saveUser (User user) ;
}

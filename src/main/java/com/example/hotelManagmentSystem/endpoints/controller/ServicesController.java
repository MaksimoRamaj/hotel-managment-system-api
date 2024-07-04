package com.example.hotelManagmentSystem.endpoints.controller;


import com.example.hotelManagmentSystem.dataproviders.dto.response.ServiceResponse;
import com.example.hotelManagmentSystem.dataproviders.service.interfaces.IServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServicesController {

    private final IServicesService hotelServicesService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<ServiceResponse>> getAll(){
        return new ResponseEntity<>(
                hotelServicesService.findAll(),
                HttpStatus.OK
        );
    }
}

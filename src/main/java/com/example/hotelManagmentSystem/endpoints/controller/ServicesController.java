package com.example.hotelManagmentSystem.endpoints.controller;


import com.example.hotelManagmentSystem.dataproviders.service.interfaces.IServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServicesController {

    private final IServicesService hotelServicesService;

    @GetMapping
    public ResponseEntity<?> getAll(){
        return new ResponseEntity<>(
                hotelServicesService.findAll(),
                HttpStatus.OK
        );
    }
}

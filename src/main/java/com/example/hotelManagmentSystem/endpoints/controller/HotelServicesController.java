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
@RequestMapping("/api/hotel-services")
@RequiredArgsConstructor
public class HotelServicesController {

    private final IServicesService iServicesService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("all")
    public ResponseEntity<List<ServiceResponse>> getHotelServices(){
        return new ResponseEntity<>(iServicesService.findAll()
                , HttpStatus.OK);
    }
}

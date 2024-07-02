package com.example.hotelManagmentSystem.dataproviders.service.interfaces;

import com.example.hotelManagmentSystem.dataproviders.dto.response.ServiceResponse;

import java.util.List;

public interface IServicesService {
    List<ServiceResponse> findAll();
}

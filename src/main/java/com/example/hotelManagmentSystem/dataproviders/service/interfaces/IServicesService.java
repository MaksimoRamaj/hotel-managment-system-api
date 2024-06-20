package com.example.hotelManagmentSystem.dataproviders.service.interfaces;

import com.example.hotelManagmentSystem.dataproviders.dto.request.ServiceResponse;
import com.example.hotelManagmentSystem.dataproviders.entity.Service;

import java.util.List;
import java.util.Set;

public interface IServicesService {
    List<ServiceResponse> findAll();
}

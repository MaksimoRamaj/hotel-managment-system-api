package com.example.hotelManagmentSystem.dataproviders.service.implementations;

import com.example.hotelManagmentSystem.dataproviders.dto.response.ServiceResponse;
import com.example.hotelManagmentSystem.dataproviders.repository.ServiceRepository;
import com.example.hotelManagmentSystem.dataproviders.service.interfaces.IServicesService;


import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServicesServiceImpl implements IServicesService {

    private final ServiceRepository serviceRepository;

    public ServicesServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<ServiceResponse> findAll() {
        return serviceRepository.findAll()
                .stream().map(service -> ServiceResponse.builder()
                        .id(service.getId())
                        .serviceName(service.getName())
                        .build())
                .collect(Collectors.toList())
                ;
    }
}

package com.example.hotelManagmentSystem.dataproviders.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ServiceResponse {
    private Integer id;
    private String serviceName;
}

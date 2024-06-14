package com.example.hotelManagmentSystem.dataproviders.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class HotelServiceResponse {
    private Integer hotelServiceId;
    private String serviceName;
}

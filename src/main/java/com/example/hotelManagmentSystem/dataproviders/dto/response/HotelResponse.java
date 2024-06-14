package com.example.hotelManagmentSystem.dataproviders.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
public class HotelResponse {
    private Integer hotelId;
    private String hotelName;
    private String admin;
    private String description;
    private int noOfRooms;
    private Set<HotelServiceResponse> hotelServices;
}

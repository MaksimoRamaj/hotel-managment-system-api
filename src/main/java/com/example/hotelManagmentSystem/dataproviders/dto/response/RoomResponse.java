package com.example.hotelManagmentSystem.dataproviders.dto.response;

import com.example.hotelManagmentSystem.dataproviders.dto.request.PriceDayDto;
import com.example.hotelManagmentSystem.dataproviders.entity.RoomPrice;
import com.example.hotelManagmentSystem.dataproviders.entity.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
public class RoomResponse {
    private int roomId;
    private String hotelName;
    private String roomType;
    private int adult;
    private int kids;
    private String description;
    private double taxRate;
    private Set<PriceDayDto> priceDayDto;
}

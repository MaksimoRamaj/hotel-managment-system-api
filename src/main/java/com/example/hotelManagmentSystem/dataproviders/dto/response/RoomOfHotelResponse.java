package com.example.hotelManagmentSystem.dataproviders.dto.response;

import com.example.hotelManagmentSystem.dataproviders.dto.request.PriceDayDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
public class RoomOfHotelResponse {
    private int roomId;
    private int hotelId;
    private String roomType;
    private int adult;
    private int kids;
    private String description;
    private double total;
    private long noOfDays;
    private ImageResponse imageResponse;
}

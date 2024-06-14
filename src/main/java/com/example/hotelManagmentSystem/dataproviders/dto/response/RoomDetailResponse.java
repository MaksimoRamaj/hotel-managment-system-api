package com.example.hotelManagmentSystem.dataproviders.dto.response;

import com.example.hotelManagmentSystem.dataproviders.entity.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
public class RoomDetailResponse{
    private int detailId;
    private String roomName;
    private int adult;
    private int kids;
    private String description;
    private String hotelName;
    private RoomType roomType;
    private Double price;
    private Boolean isAvailable;
    private LocalDate date;
}

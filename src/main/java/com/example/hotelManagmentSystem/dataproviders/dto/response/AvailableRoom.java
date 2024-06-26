package com.example.hotelManagmentSystem.dataproviders.dto.response;

import com.example.hotelManagmentSystem.dataproviders.entity.Hotel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AvailableRoom {
    private Hotel hotel;
    private int count;
}

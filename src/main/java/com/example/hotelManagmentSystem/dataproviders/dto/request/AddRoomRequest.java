package com.example.hotelManagmentSystem.dataproviders.dto.request;

import com.example.hotelManagmentSystem.dataproviders.entity.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class AddRoomRequest {
    private String roomType;
    private Set<PriceDayDto> priceDayDto;
    private String description;
    private int adult;
    private int kids;
    private Integer hotelId;
}

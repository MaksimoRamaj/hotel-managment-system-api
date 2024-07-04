package com.example.hotelManagmentSystem.dataproviders.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.Set;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class PriceDayDto {
    private DayOfWeek dayOfWeek;
    private Double price;
}

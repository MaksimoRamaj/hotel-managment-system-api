package com.example.hotelManagmentSystem.dataproviders.dto.response;

import com.example.hotelManagmentSystem.dataproviders.entity.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class ReservationHistoryResponse {
    private String hotelName;
    private double total;
    private LocalDate checkIn;
    private ReservationStatus status;
    private int currentPage;
    private int pageSize;
    private int totalPages;
}

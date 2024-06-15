package com.example.hotelManagmentSystem.dataproviders.dto.response;

import com.example.hotelManagmentSystem.dataproviders.entity.*;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class ReservationResponse {
    private Integer reservationId;
    private String hotelName;
    private String clientName;
    private String roomName;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Integer adults;
    private Integer kids;
    private Double netValue;
    private Double taxRate;
    private Double discount;
    private Double total;
    private LocalDate createdAt;
}

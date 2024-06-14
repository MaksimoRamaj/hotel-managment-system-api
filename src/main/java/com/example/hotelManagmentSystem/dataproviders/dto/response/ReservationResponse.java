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
    private Integer billDetailId;
//    private String hotelName;
//    private String clientUsername;
//    private String roomName;
    private LocalDate checkIn;
    private LocalDate checkOut;
//    private Integer adults;
//    private Integer kids;
    private Boolean isPaid;

    private Integer taxRate;
    private Double roomPrice;
    private Double extraServiceCharge;
    private Double discount;
    private Integer totalDays;
    private Double totalAmount;
    private Boolean cash;
    private double score;
    private int totalReservations;
    //fund billDetail
}

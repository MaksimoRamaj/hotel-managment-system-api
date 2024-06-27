package com.example.hotelManagmentSystem.dataproviders.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private User client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "check_in")
    private LocalDate checkIn;

    @Column(name = "check_out")
    private LocalDate checkOut;

    @Column(name = "adults")
    private Integer adults;

    @Column(name = "kids")
    private Integer kids;

    @Column(name = "net_value")
    private Double netValue;

    @Column(name = "tax_rate")
    private Double taxRate;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "total")
    private Double total;

    @Column(name = "status")
    private ReservationStatus status;

}
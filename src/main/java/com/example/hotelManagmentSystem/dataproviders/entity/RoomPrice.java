package com.example.hotelManagmentSystem.dataproviders.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "room_price")
public class RoomPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "price")
    private Double price;

    @Column(name = "day")
    private DayOfWeek day;

}
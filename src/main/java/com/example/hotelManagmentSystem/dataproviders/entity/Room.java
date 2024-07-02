package com.example.hotelManagmentSystem.dataproviders.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @NotBlank(message = "Room type field must not be blank!")
    @Column(name = "type")
    private String type;

    @Column(name = "adult")
    private Integer adult;

    @Column(name = "kids")
    private Integer kids;

    @NotBlank(message = "Description field must not be blank!")
    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER)
    private Set<RoomPrice> roomPrices;

}
package com.example.hotelManagmentSystem.dataproviders.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "hotel_images")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @Column(name = "name", length = Integer.MAX_VALUE)
    private String name;

    @Column(name = "type", length = Integer.MAX_VALUE)
    private String type;

    @Column(name = "url", length = Integer.MAX_VALUE)
    private String url;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

}
package com.example.hotelManagmentSystem.dataproviders.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "hotel")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private User admin;

    @Column(name = "name", length = Integer.MAX_VALUE)
    private String name;

    @Column(name = "address")
    private Integer address;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @OneToMany(mappedBy = "hotel")
    private Set<HotelService> hotelServices = new LinkedHashSet<>();

    @Column(name = "tax_rate")
    private Double taxRate;

}
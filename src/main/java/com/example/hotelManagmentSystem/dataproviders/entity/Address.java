package com.example.hotelManagmentSystem.dataproviders.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "street", length = Integer.MAX_VALUE)
    private String street;

    @Column(name = "city", length = Integer.MAX_VALUE)
    private String city;

    @Column(name = "state", length = Integer.MAX_VALUE)
    private String state;

    @Column(name = "zip_code")
    private Integer zipCode;

}
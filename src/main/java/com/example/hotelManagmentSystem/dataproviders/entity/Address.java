package com.example.hotelManagmentSystem.dataproviders.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Address field city, must be filled!")
    @Column(name = "city", length = Integer.MAX_VALUE)
    private String city;

    @NotBlank(message = "Address field state, must be filled!")
    @Column(name = "state", length = Integer.MAX_VALUE)
    private String state;

    @Column(name = "zip_code")
    private Integer zipCode;

}
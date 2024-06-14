package com.example.hotelManagmentSystem.dataproviders.dto.response;

import com.example.hotelManagmentSystem.dataproviders.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientAuthResponse {
    private String token;
    private String role;
    private int totalReservations;
    private double score;
    private String firstName;
    private String lastName;
    private Address address;
}

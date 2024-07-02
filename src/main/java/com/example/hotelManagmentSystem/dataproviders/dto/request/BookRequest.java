package com.example.hotelManagmentSystem.dataproviders.dto.request;

import com.example.hotelManagmentSystem.dataproviders.entity.Address;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class BookRequest {
    private int roomId;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate CheckIn;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate CheckOut;
    @NotBlank(message = "Net Value must not be blank!")
    private double netValue;
    @NotBlank(message = "Discount must not be blank!")
    private double discount;
    @NotBlank(message = "Total after tax must not be blank!")
    private double totalAfterTax;
    @NotBlank(message = "FullName must not be blank!")
    private String fullName;
    @NotBlank(message = "Email must not be blank!")
    private String email;
    @NotBlank(message = "Address must not be blank!")
    private Address address;
    @NotBlank(message = "Phone Number must not be blank!")
    private String phoneNumber;
    @NotBlank(message = "Name On Card must not be blank!")
    private String nameOnCard;
    @NotBlank(message = "Number On Card must not be blank!")
    private String numberOnCard;
    @NotBlank(message = "Expire Card must not be blank!")
    private String expireCard;
    @NotBlank(message = "CVV must not be blank!")
    private String cvv;
}

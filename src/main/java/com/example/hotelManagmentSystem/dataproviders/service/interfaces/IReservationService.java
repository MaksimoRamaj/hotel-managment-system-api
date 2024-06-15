package com.example.hotelManagmentSystem.dataproviders.service.interfaces;

import com.example.hotelManagmentSystem.dataproviders.dto.request.BookRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.response.ReservationResponse;
import com.example.hotelManagmentSystem.dataproviders.entity.Reservation;

public interface IReservationService {
    ReservationResponse book(BookRequest request, String userEmail);
}

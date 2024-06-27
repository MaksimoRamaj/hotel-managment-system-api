package com.example.hotelManagmentSystem.dataproviders.service.interfaces;

import com.example.hotelManagmentSystem.dataproviders.dto.request.BookRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.response.ReservationHistoryResponse;
import com.example.hotelManagmentSystem.dataproviders.dto.response.ReservationResponse;
import com.example.hotelManagmentSystem.dataproviders.entity.Reservation;

import java.util.Set;

public interface IReservationService {
    ReservationResponse book(BookRequest request, String userEmail);

    Set<ReservationHistoryResponse> getReservationsByUser(String userEmail,int pageNumber,int pageSize);
}

package com.example.hotelManagmentSystem.dataproviders.service.interfaces;

import com.example.hotelManagmentSystem.dataproviders.dto.request.AddHotelRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.request.AvailabilityRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.response.HotelResponse;

import java.io.IOException;
import java.util.Set;

public interface IHotelService {
//    public Set<HotelResponse> getAvailableHotelsFilterByRoomsAvailability(CheckAvailabilityRequest request);
    public HotelResponse addHotel(AddHotelRequest addHotelRequest,String userEmail);

    Set<HotelResponse> findAll();

    Set<HotelResponse> findAllHotelsByUser(String userEmail);

    Set<HotelResponse> findAvailableHotels(AvailabilityRequest request,int pageNumber,int pageSize);
}

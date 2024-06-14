package com.example.hotelManagmentSystem.dataproviders.service.interfaces;

import com.example.hotelManagmentSystem.dataproviders.dto.request.*;
import com.example.hotelManagmentSystem.dataproviders.dto.response.ReservationResponse;
import com.example.hotelManagmentSystem.dataproviders.dto.response.RoomDetailResponse;
import com.example.hotelManagmentSystem.dataproviders.dto.response.RoomResponse;

import java.util.LinkedList;
import java.util.Set;

public interface IRoomService {
    RoomResponse addRoom(AddRoomRequest request,String userEmail);

    Set<RoomResponse> getRoomByHotelId(Integer hotelId,AvailabilityRequest request);

    Set<RoomResponse> findAvailableRooms(AvailabilityRequest request);
}

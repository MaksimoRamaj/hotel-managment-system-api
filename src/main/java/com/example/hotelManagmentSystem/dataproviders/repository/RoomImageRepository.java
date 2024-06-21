package com.example.hotelManagmentSystem.dataproviders.repository;

import com.example.hotelManagmentSystem.dataproviders.entity.HotelImage;
import com.example.hotelManagmentSystem.dataproviders.entity.RoomImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface RoomImageRepository extends JpaRepository<RoomImage, Integer> {
    Optional<RoomImage> findByName(String fileName);
    Set<RoomImage> findByRoomId(Integer roomId);
}
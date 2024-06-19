package com.example.hotelManagmentSystem.dataproviders.repository;

import com.example.hotelManagmentSystem.dataproviders.entity.Hotel;
import com.example.hotelManagmentSystem.dataproviders.entity.HotelImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface HotelImageRepository extends JpaRepository<HotelImage, Integer> {
    Optional<HotelImage> findByName(String fileName);
    Set<HotelImage> findByHotelId(Integer hotelId);
}
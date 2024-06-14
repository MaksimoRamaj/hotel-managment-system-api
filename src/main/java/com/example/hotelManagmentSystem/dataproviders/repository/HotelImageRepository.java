package com.example.hotelManagmentSystem.dataproviders.repository;

import com.example.hotelManagmentSystem.dataproviders.entity.HotelImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HotelImageRepository extends JpaRepository<HotelImage, Integer> {
    Optional<HotelImage> findByName(String fileName);
}
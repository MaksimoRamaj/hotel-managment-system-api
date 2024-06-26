package com.example.hotelManagmentSystem.dataproviders.repository;

import com.example.hotelManagmentSystem.dataproviders.dto.response.AvailableRoom;
import com.example.hotelManagmentSystem.dataproviders.entity.Hotel;
import com.example.hotelManagmentSystem.dataproviders.entity.Room;
import io.micrometer.common.lang.NonNullApi;
import io.micrometer.common.lang.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {
    List<Hotel> findAllByAdmin_Id(Integer adminId);

    @Query("SELECT r.hotel, COUNT(r) " +
            "FROM Room r " +
            "WHERE r.id NOT IN (" +
            "SELECT res.room.id " +
            "FROM Reservation res " +
            "WHERE :checkInDate < res.checkOut AND :checkOutDate > res.checkIn) " +
            "AND r.kids = :kids AND r.adult = :adults " +
            "GROUP BY r.hotel")
    Page<Object[]> findAvailableHotels(
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("kids") int kids,
            @Param("adults") int adults,
            Pageable pageable
    );

    @Query("SELECT r.hotel, COUNT(r) " +
            "FROM Room r " +
            "WHERE r.id NOT IN (" +
            "SELECT res.room.id " +
            "FROM Reservation res " +
            "WHERE :checkInDate < res.checkOut AND :checkOutDate > res.checkIn) " +
            " AND r.adult = :adults " +
            "GROUP BY r.hotel")
    Page<Object[]> findAvailableHotels(
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("adults") int adults,
            Pageable pageable
    );

    @Query("SELECT r.hotel, COUNT(r) " +
            "FROM Room r " +
            "WHERE r.id NOT IN (" +
            "SELECT res.room.id " +
            "FROM Reservation res " +
            "WHERE :checkInDate < res.checkOut AND :checkOutDate > res.checkIn) " +
            "GROUP BY r.hotel")
    Page<Object[]> findAvailableHotels(
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            Pageable pageable
    );

    @Query("SELECT r.hotel, COUNT(r) " +
            "FROM Room r " +
            "WHERE r.id NOT IN (" +
            "SELECT res.room.id " +
            "FROM Reservation res " +
            "WHERE :checkInDate < res.checkOut AND :checkOutDate > res.checkIn) " +
            "AND r.kids = :kids " +
            "GROUP BY r.hotel")
    Page<Object[]> findAvailableHotelsWhenKidsPresent(
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("kids") int kids,
            Pageable pageable
    );

}
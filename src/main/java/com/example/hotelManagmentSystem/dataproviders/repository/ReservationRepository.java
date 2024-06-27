package com.example.hotelManagmentSystem.dataproviders.repository;

import com.example.hotelManagmentSystem.dataproviders.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query("select res from Reservation res where " +
            ":checkInDate < res.checkOut and :checkOutDate > res.checkIn " +
            "and res.kids = :kids and res.adults = :adults")
    List<Reservation> findReservations(
            LocalDate checkInDate,
            LocalDate checkOutDate,
            int kids,
            int adults
    );

    Page<Reservation> findAllByClientId(int clientId, Pageable pageable);

}
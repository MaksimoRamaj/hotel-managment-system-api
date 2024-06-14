package com.example.hotelManagmentSystem.dataproviders.repository;

import com.example.hotelManagmentSystem.dataproviders.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findAllByHotelId(Integer hotelId);

    @Query("SELECT r FROM Room r WHERE r.id NOT IN (" +
            "SELECT r.id FROM Room r INNER JOIN Reservation rv ON r.id = rv.room.id " +
            "WHERE :newCheckIn < rv.checkOut AND :newCheckOut > rv.checkIn " +
            "AND r.adult <> :adult AND r.kids <> :kids)")
    List<Room> findAvailableRooms(
            @Param("newCheckIn") LocalDate newCheckIn,
            @Param("newCheckOut") LocalDate newCheckOut,
            @Param("adult") int adult,
            @Param("kids") int kids);

}
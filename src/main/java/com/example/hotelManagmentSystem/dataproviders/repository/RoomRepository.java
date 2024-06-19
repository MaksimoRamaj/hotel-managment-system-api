package com.example.hotelManagmentSystem.dataproviders.repository;

import com.example.hotelManagmentSystem.dataproviders.entity.Room;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer>
{
    List<Room> findAllByHotelId(Integer hotelId);

    @Query(value = "SELECT * FROM \"room\" r WHERE r.\"id\" not IN (" +
            "SELECT res.\"room_id\" FROM \"reservation\" res " +
            "WHERE (?1 < res.\"check_out\" AND ?2 > res.\"check_in\") " +
            "AND (r.\"kids\" = ?3 AND r.\"adult\" = ?4))", nativeQuery = true)
    Set<Room> findAvailableRooms(LocalDate checkInDate,
                                 LocalDate checkOutDate,
                                 int kids,
                                 int adults);




    @Query("SELECT r FROM Room r WHERE r.id NOT IN (" +
            "SELECT r.id FROM Room r INNER JOIN Reservation rv ON r.id = rv.room.id " +
            "WHERE :newCheckIn < rv.checkOut AND :newCheckOut > rv.checkIn " +
            "AND r.adult = :adult AND r.kids = :kids) AND r.id = :roomId")
    Optional<Room> findAvailableRoom(
            @Param("newCheckIn") LocalDate newCheckIn,
            @Param("newCheckOut") LocalDate newCheckOut,
            @Param("adult") int adult,
            @Param("kids") int kids,
            @Param("roomId") int roomId);

}
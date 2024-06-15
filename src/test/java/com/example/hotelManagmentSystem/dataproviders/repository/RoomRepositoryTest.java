package com.example.hotelManagmentSystem.dataproviders.repository;

import com.example.hotelManagmentSystem.dataproviders.entity.Room;
import com.example.hotelManagmentSystem.dataproviders.entity.Reservation;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    @Order(1)
    @Rollback(false) // Prevent rollback to keep data for the next tests
    void setup() {
        // Create test rooms
        Room room1 = new Room();
        room1.setAdult(2);
        room1.setKids(2);
        room1 = roomRepository.save(room1);

        Room room2 = new Room();
        room2.setAdult(2);
        room2.setKids(2);
        room2 = roomRepository.save(room2);

        // Create a reservation for room1
        Reservation reservation = new Reservation();
        reservation.setRoom(room1);
        reservation.setCheckIn(LocalDate.of(2024, 6, 15));
        reservation.setCheckOut(LocalDate.of(2024, 6, 16));
        reservation.setAdults(2);
        reservation.setKids(2);
        reservationRepository.save(reservation);
    }

    @Test
    @Order(2)
    void findAvailableRooms() {
        List<Room> rooms = roomRepository.findAvailableRooms(
                LocalDate.of(2024,6,14),
                LocalDate.of(2024,6,17),
                2,2
        );

        assertEquals(1, rooms.size());
        assertEquals(2, rooms.get(0).getAdult());
        assertEquals(2, rooms.get(0).getKids());
    }

    @Test
    @Order(3)
    void findAvailableRoom() {
        Optional<Room> room = roomRepository.findAvailableRoom(
                LocalDate.of(2024,6,14),
                LocalDate.of(2024,6,17),
                2,2,
                2 // assuming room2 has ID 2
        );

        assertTrue(room.isPresent());
        assertEquals(2, room.get().getAdult());
        assertEquals(2, room.get().getKids());
    }
}

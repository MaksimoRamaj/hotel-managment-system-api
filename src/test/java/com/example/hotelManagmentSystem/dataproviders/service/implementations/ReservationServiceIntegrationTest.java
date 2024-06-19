package com.example.hotelManagmentSystem.dataproviders.service.implementations;

import static org.junit.jupiter.api.Assertions.*;


import com.example.hotelManagmentSystem.dataproviders.dto.request.BookRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.response.ReservationResponse;
import com.example.hotelManagmentSystem.dataproviders.entity.*;
import com.example.hotelManagmentSystem.dataproviders.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ReservationServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ClientLogRepository clientLogRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ReservationServiceImpl reservationService;

    @Autowired
    private RoomPriceRepository roomPriceRepository;

    private User testUser;
    private Room testRoom;
    private Hotel testHotel;

    @BeforeEach
    public void setup() {
        testHotel = Hotel.builder()
                .name("Test Hotel")
                .taxRate(10.0)
                .build();
        hotelRepository.save(testHotel);

        testUser = userRepository.findById(1).get();

        testRoom = Room.builder()
                .hotel(testHotel)
                .type("Deluxe")
                .adult(2)
                .kids(2)
                .roomPrices(new HashSet<>())
                .build();
        roomRepository.save(testRoom);

        // Add room prices for each day of the week
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            testRoom.getRoomPrices().add(RoomPrice.builder()
                    .room(testRoom)
                    .day(dayOfWeek)
                    .price(100.0)
                    .build());
        }
        roomPriceRepository.saveAll(testRoom.getRoomPrices());
        roomRepository.save(testRoom);
    }

    @Test
    public void testBookRoomSuccessfully() {
        BookRequest bookRequest = BookRequest.builder()
                .roomId(testRoom.getId())
                .CheckIn(LocalDate.of(2024, 6, 14))
                .CheckOut(LocalDate.of(2024, 6, 17))
                .netValue(300.0)
                .discount(0.0)
                .totalAfterTax(330.0)
                .build();

        ReservationResponse response = reservationService.book(bookRequest, testUser.getEmail());

        assertNotNull(response);
        assertEquals("Test Hotel", response.getHotelName());
        assertEquals("Deluxe", response.getRoomName());
        assertEquals(LocalDate.of(2024, 6, 14), response.getCheckIn());
        assertEquals(LocalDate.of(2024, 6, 17), response.getCheckOut());
        assertEquals(2, response.getAdults());
        assertEquals(2, response.getKids());
        assertEquals(300.0, response.getNetValue());
        assertEquals(10.0, response.getTaxRate());
        assertEquals(0.0, response.getDiscount());
        assertEquals(330.0, response.getTotal());
    }

    @Test
    public void testFindAvailableRooms() {
        BookRequest bookRequest = BookRequest.builder()
                .roomId(testRoom.getId())
                .CheckIn(LocalDate.of(2024, 6, 14))
                .CheckOut(LocalDate.of(2024, 6, 17))
                .netValue(300.0)
                .discount(0.0)
                .totalAfterTax(330.0)
                .build();

        //conflicting reservation
        Reservation conflictingReservation = Reservation.builder()
                .hotel(testHotel)
                .client(testUser)
                .room(testRoom)
                .checkIn(LocalDate.of(2024, 6, 14))
                .checkOut(LocalDate.of(2024, 6, 17))
                .adults(2)
                .kids(2)
                .netValue(300.0)
                .taxRate(10.0)
                .total(330.0)
                .createdAt(LocalDate.now())
                .build();
        reservationRepository.save(conflictingReservation);

    }

    // Add more tests as needed to cover other scenarios
}

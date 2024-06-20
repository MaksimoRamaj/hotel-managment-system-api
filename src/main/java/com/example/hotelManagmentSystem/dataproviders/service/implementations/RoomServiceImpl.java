package com.example.hotelManagmentSystem.dataproviders.service.implementations;

import com.example.hotelManagmentSystem.core.exceptions.InvalidRequestException;
import com.example.hotelManagmentSystem.core.exceptions.InvalidRoomPriceException;
import com.example.hotelManagmentSystem.dataproviders.dto.request.AddRoomRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.request.AvailabilityRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.request.PriceDayDto;
import com.example.hotelManagmentSystem.dataproviders.dto.response.ReservationResponse;
import com.example.hotelManagmentSystem.dataproviders.dto.response.RoomDetailResponse;
import com.example.hotelManagmentSystem.dataproviders.dto.response.RoomResponse;
import com.example.hotelManagmentSystem.dataproviders.entity.*;
import com.example.hotelManagmentSystem.dataproviders.repository.*;
import com.example.hotelManagmentSystem.dataproviders.service.interfaces.IRoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements IRoomService {
    private final RoomPriceRepository roomPriceRepository;

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;

    @Override
    public RoomResponse addRoom(AddRoomRequest request,String userEmail) {

        User user = userRepository.findUserByEmail(userEmail).get();
        Hotel hotel = hotelRepository.findById(request.getHotelId()).get();

        if (!isPriceDayDtoValid(request.getPriceDayDto())){
            throw new InvalidRoomPriceException("Price should be > 0 or " +
                    "fill prices for all weekdays, no repeated days allowed!");
        };

        if (hotel.getAdmin().getId().intValue() != user.getId().intValue()){
            throw new InvalidRequestException("You should be the owner of the hotel to add the room!");
        }

        Room room = Room.builder()
                .adult(request.getAdult())
                .kids(request.getKids())
                .description(request.getDescription())
                .hotel(hotel)
                .type(request.getRoomType())
                .build();

        Set<RoomPrice> roomPrices = request.getPriceDayDto().stream()
                .map(priceDayDto -> RoomPrice.builder()
                .day(priceDayDto.getDayOfWeek())
                .price(priceDayDto.getPrice())
                .room(room)
                .build()).collect(Collectors.toSet());

        room.setRoomPrices(roomPrices);

        roomRepository.save(room);
        roomPriceRepository.saveAll(roomPrices);

        return mapToRoomResponse(room);
    }

    @Override
    public Set<RoomResponse> getRoomByHotelId(Integer hotelId,AvailabilityRequest request) {
        Set<Room> rooms = roomRepository.findAvailableRooms(request.getCheckIn(),
                request.getCheckOut(),
                request.getAdult(),
                request.getKids());

        return rooms.stream()
                .filter(room -> room.getHotel().getId().intValue() == hotelId)
                .map(this::mapToRoomResponse)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<RoomResponse> findAvailableRooms(AvailabilityRequest request) {
        Set<Room> rooms = roomRepository.findAvailableRooms(request.getCheckIn(),
                request.getCheckOut(),
                request.getAdult(),
                request.getKids());



        return null;
    }

    private RoomResponse mapToRoomResponse(Room room){

        return RoomResponse.builder()
                .roomId(room.getId())
                .taxRate(room.getHotel().getTaxRate())
                .roomType(room.getType())
                .adult(room.getAdult())
                .kids(room.getKids())
                .description(room.getDescription())
                .hotelName(room.getHotel().getName())
                .priceDayDto(room.getRoomPrices().stream()
                        .map(roomPrice -> PriceDayDto.builder()
                                .dayOfWeek(roomPrice.getDay())
                                .price(roomPrice.getPrice())
                                .build()).collect(Collectors.toSet()))
                .build();
    }

    private boolean isPriceDayDtoValid(Set<PriceDayDto> priceDayDtos) {
        if (priceDayDtos == null) {
            return false;
        }
        Set<DayOfWeek> weekDays = priceDayDtos.stream()
                .map(PriceDayDto::getDayOfWeek)
                .collect(Collectors.toSet());

        for (DayOfWeek day : DayOfWeek.values()) {
            if (!weekDays.contains(day)) {
                return false;
            }
        }
        //kontrolloj nese ka vendosur me shume se 7 dite
        if (priceDayDtos.size() > 7){
            return false;
        }

        for (PriceDayDto priceDayDto : priceDayDtos){
            if (priceDayDto.getPrice() <= 0){
                return false;
            }
        }

        return true;
    }


}

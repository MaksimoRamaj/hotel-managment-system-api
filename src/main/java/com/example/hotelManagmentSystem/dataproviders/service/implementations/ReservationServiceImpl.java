package com.example.hotelManagmentSystem.dataproviders.service.implementations;

import com.example.hotelManagmentSystem.core.exceptions.BookingException;
import com.example.hotelManagmentSystem.dataproviders.dto.request.BookRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.response.ReservationResponse;
import com.example.hotelManagmentSystem.dataproviders.entity.Reservation;
import com.example.hotelManagmentSystem.dataproviders.entity.Room;
import com.example.hotelManagmentSystem.dataproviders.entity.User;
import com.example.hotelManagmentSystem.dataproviders.repository.ClientLogRepository;
import com.example.hotelManagmentSystem.dataproviders.repository.ReservationRepository;
import com.example.hotelManagmentSystem.dataproviders.repository.RoomRepository;
import com.example.hotelManagmentSystem.dataproviders.repository.UserRepository;
import com.example.hotelManagmentSystem.dataproviders.service.interfaces.IReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ClientLogRepository clientLogRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public ReservationResponse book(BookRequest request, String userEmail) {

        User user = userRepository.findUserByEmail(userEmail).get();
        Room room = roomRepository.findById(request.getRoomId()).get();

        //kontrollo nese dhoma eshte ende available ne datat e kerkuara
        if (roomRepository.findAvailableRoom(request.getCheckIn(),
                request.getCheckOut(),
                room.getAdult(),
                room.getKids(),
                room.getId()).isEmpty()){
            throw new BookingException("Room not available!");
        };

        //llogarit net value per ditet e qendrimit dhe krahasoje me vleren e ardhur
        //prej front end

        HashMap<DayOfWeek,Double> dayOfWeekDoubleHashMap = new HashMap<>();
                room.getRoomPrices()
                        .stream()
                        .forEach(roomPrice ->
                                dayOfWeekDoubleHashMap
                                        .put(roomPrice.getDay(),roomPrice.getPrice()));

        Double netValue = 0.0;
        LocalDate currentDate = request.getCheckIn();
        while (!currentDate.isAfter(request.getCheckOut().minusDays(1))) {
            Double roomPrice = dayOfWeekDoubleHashMap.get(currentDate.getDayOfWeek());
            netValue+=roomPrice;
            currentDate = currentDate.plusDays(1);
        }

        if (netValue.compareTo(request.getNetValue()) != 0 ){
            throw new BookingException("Gabim ne llogaritjen e net value!");
        }

        Reservation reservation = Reservation.builder()
                .hotel(room.getHotel())
                .client(user)
                .room(room)
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .adults(room.getAdult())
                .kids(room.getKids())
                .netValue(netValue)
                .taxRate(room.getHotel().getTaxRate())
                .build();

        //llogarit discount nese eshte aplikuar
        //useri mund te kerkoj deri ne 10% discount
        //ne vleren neto
        //nese ploteson piket
        //dhe kontrolloje me vleren e discount te ardhur
        //prej front-end
        //1 score == 1$

        if (request.getDiscount() != 0) {
            //nese eshte aplikuar discount
            //kontrollo nese useri i ka piket per te perfituar 10% discount
            if (user.getClientLog().getScore() < (netValue) * 0.1){
                throw new BookingException("Useri nuk ka mjaftushem pike" +
                        "per te perfituar discount!");
            }
            //nese e perfiton discount kontrollo nese vlera eshte e sakte
            if (request.getDiscount() > (netValue*0.1)){
                throw new BookingException("Useri nuk mund te perfitoj me shume se 10% discount");
            }
            //nese e ka perfituar sakte
            user.getClientLog().setScore(user.getClientLog().getScore()-request.getDiscount());

            netValue -= request.getDiscount();
            reservation.setDiscount(request.getDiscount());
        }else {
            //ne rast te kundert perfito score 3% te vleres neto
            user.getClientLog().setScore(user.getClientLog().getScore()+(netValue*0.03));
        }

        //aplko taksen
        double afterTax =netValue+netValue * (room.getHotel().getTaxRate()/100);

        if (afterTax != request.getTotalAfterTax()){
            throw new BookingException("Wrong total after tax!");
        }

        reservation.setTotal(afterTax);
        reservation.setCreatedAt(LocalDate.now());
        user.getClientLog().setTotalReservations(user.getClientLog().getTotalReservations().intValue()+1);
        clientLogRepository.save(user.getClientLog());
        reservation = reservationRepository.save(reservation);
        return ReservationResponse.builder()
                .reservationId(reservation.getId())
                .hotelName(reservation.getHotel().getName())
                .clientName(reservation.getClient().getFirstname() + " " +
                        reservation.getClient().getLastname())
                .roomName(reservation.getRoom().getType())
                .checkIn(reservation.getCheckIn())
                .checkOut(reservation.getCheckOut())
                .adults(reservation.getAdults())
                .kids(reservation.getKids())
                .netValue(reservation.getNetValue())
                .taxRate(reservation.getTaxRate())
                .discount(reservation.getDiscount())
                .createdAt(reservation.getCreatedAt())
                .total(reservation.getTotal())
                .build();

    }
}

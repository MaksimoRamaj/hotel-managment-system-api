package com.example.hotelManagmentSystem.dataproviders.service.implementations;

import com.example.hotelManagmentSystem.core.exceptions.BookingException;
import com.example.hotelManagmentSystem.core.exceptions.InvalidRequestException;
import com.example.hotelManagmentSystem.dataproviders.dto.request.BookRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.response.ReservationHistoryResponse;
import com.example.hotelManagmentSystem.dataproviders.dto.response.ReservationResponse;
import com.example.hotelManagmentSystem.dataproviders.entity.Reservation;
import com.example.hotelManagmentSystem.dataproviders.entity.ReservationStatus;
import com.example.hotelManagmentSystem.dataproviders.entity.Room;
import com.example.hotelManagmentSystem.dataproviders.entity.User;
import com.example.hotelManagmentSystem.dataproviders.repository.ClientLogRepository;
import com.example.hotelManagmentSystem.dataproviders.repository.ReservationRepository;
import com.example.hotelManagmentSystem.dataproviders.repository.RoomRepository;
import com.example.hotelManagmentSystem.dataproviders.repository.UserRepository;
import com.example.hotelManagmentSystem.dataproviders.service.interfaces.IReservationService;
import com.example.hotelManagmentSystem.validators.EmailValidator;
import com.example.hotelManagmentSystem.validators.PhoneNumberValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements IReservationService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ClientLogRepository clientLogRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public ReservationResponse book(BookRequest request, String userEmail) {

        User user = userRepository.findUserByEmail(userEmail).get();
        Optional<Room> opRoom = roomRepository.findById(request.getRoomId());

        if (opRoom.isEmpty()){
            throw new BookingException("Wrong room id!");
        }

        Room room = opRoom.get();

        if (request.getCheckIn().isAfter(request.getCheckOut())){
            throw new BookingException("Check-in nuk mund te jete me vone check-out!");
        }
        if (request.getCheckIn().isBefore(LocalDate.now())){
            throw new InvalidRequestException("Check-in not valid!");
        }

        clientCredentialsValidator(request);
        creditCardValidator(request);

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
                .status(ReservationStatus.CONFIRMED)
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
                .status(reservation.getStatus())
                .total(reservation.getTotal())
                .build();

    }

    private void creditCardValidator(BookRequest bookRequest){
        if (bookRequest.getNameOnCard() == null || bookRequest.getNameOnCard().isEmpty()){
            throw new InvalidRequestException("You should provide a name for the card!");
        }
        if (bookRequest.getExpireCard() == null ||
             bookRequest.getExpireCard().isEmpty()){
            throw new InvalidRequestException("You should provide an expiration date!");
        }

        String expireDateRegex = "^(0[1-9]|1[0-2])/([0-9]{2})$";
        //kontrollo formatin
        if(!Pattern.compile(expireDateRegex)
                .matcher(bookRequest.getExpireCard()).matches()){
            throw new InvalidRequestException("ExpireDate format not valid!");
        }

        YearMonth cardYearMonth =
                YearMonth.of(Integer.parseInt(bookRequest.getExpireCard().substring(3,5)+2000),
                        Integer.parseInt(bookRequest.getExpireCard().substring(0,2)));

        if (cardYearMonth.isBefore(YearMonth.now())){
            throw new BookingException("Credit Card is expired!");
        }

        String cvv = bookRequest.getCvv().replaceAll("[^\\d]", "");

        if (cvv.length() != 3 || (Integer.parseInt(cvv) < 0)){
            throw new BookingException("Invalid cvv!");
        }
        if (bookRequest.getNumberOnCard() == null ||
            bookRequest.getNumberOnCard().length() != 16 ||
            containsNonNumericCharacters(bookRequest.getNumberOnCard())
        ){
            throw new BookingException("Invalid card number!");
        }
    }

    private  boolean containsNonNumericCharacters(String str) {
        Pattern pattern = Pattern.compile("[^\\d]");
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    private void clientCredentialsValidator(BookRequest bookRequest){
        if (bookRequest.getFullName() == null || bookRequest.getFullName().isEmpty()){
            throw new InvalidRequestException("Full Name is missing or is empty!");
        }
        boolean isValid = EmailValidator.validate(bookRequest.getEmail());
        if (!isValid){
            throw new InvalidRequestException("Email format not valid!");
        }
        if (bookRequest.getAddress() == null){
            throw new InvalidRequestException("You should provide an address!");
        }
        if (bookRequest.getPhoneNumber() == null || bookRequest.getPhoneNumber().isEmpty()
                || (!PhoneNumberValidator.validate(bookRequest.getPhoneNumber()))){
            throw new InvalidRequestException("You should provide a valid phone number!");
        }

    }

    @Override
    public Set<ReservationHistoryResponse> getReservationsByUser(String userEmail,int pageNumber,int pageSize) {
        User user = userRepository.findUserByEmail(userEmail).get();

        PageRequest requestPage = PageRequest.of(pageNumber,pageSize);

        Page<Reservation> reservations = reservationRepository
                .findAllByClientId(user.getId(),requestPage);

           return reservations.get()
                    .map(reservation ->
                            ReservationHistoryResponse.builder()
                                    .hotelName(reservation.getHotel().getName())
                                    .total(reservation.getTotal())
                                    .checkIn(reservation.getCheckIn())
                                    .status(reservation.getStatus())
                                    .currentPage(pageNumber)
                                    .pageSize(pageSize)
                                    .totalPages(reservations.getTotalPages())
                                    .build()).collect(Collectors.toSet());
    }
}

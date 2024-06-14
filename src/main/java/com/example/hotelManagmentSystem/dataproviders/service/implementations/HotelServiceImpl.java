package com.example.hotelManagmentSystem.dataproviders.service.implementations;

import com.example.hotelManagmentSystem.dataproviders.dto.request.AddHotelRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.request.AvailabilityRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.response.HotelResponse;
import com.example.hotelManagmentSystem.dataproviders.dto.response.HotelServiceResponse;
import com.example.hotelManagmentSystem.dataproviders.entity.*;
import com.example.hotelManagmentSystem.dataproviders.repository.*;
import com.example.hotelManagmentSystem.dataproviders.service.interfaces.IHotelService;
import com.example.hotelManagmentSystem.dataproviders.entity.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class HotelServiceImpl implements IHotelService {
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final HotelServiceRepository hotelServiceRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

//    @Override
//    public Set<HotelResponse> getAvailableHotelsFilterByRoomsAvailability(CheckAvailabilityRequest request) {
//
//    }

    @Override
    public HotelResponse addHotel(AddHotelRequest addHotelRequest,String userEmail){
        User user = userRepository.findUserByEmail(userEmail).get();

        Hotel hotelToAdd = Hotel
                .builder()
                .name(addHotelRequest.getHotelName())
                .admin(user)
                .description(addHotelRequest.getHotelDesc())
                .taxRate(addHotelRequest.getTaxRate())
                .build();

        List<Service> services = serviceRepository.findAllById(addHotelRequest.getHotelServices());

        Set<HotelService> hotelServices = services.stream().map(
                service -> mapHotelServicesToHotelService(service,hotelToAdd)
        ).collect(Collectors.toSet());

        hotelRepository.save(hotelToAdd);

        hotelServiceRepository.saveAll(hotelServices);

        return mapHotelToHotelResponse(hotelToAdd,hotelServices);
    }


    private HotelService mapHotelServicesToHotelService(Service service, Hotel hotelToAdd) {
        return HotelService.builder()
                .hotel(hotelToAdd)
                .service(service)
                .build();
    }


    @Override
    public Set<HotelResponse> findAll() {
       return hotelRepository.findAll()
               .stream()
               .map(this::mapHotelToHotelResponse)
               .collect(Collectors.toSet());
    }

    @Override
    public Set<HotelResponse> findAllHotelsByUser(String userEmail) {
        return hotelRepository.findAllByAdmin_Id(userRepository.findUserByEmail(userEmail).get().getId())
                .stream()
                .map(this::mapHotelToHotelResponse)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<HotelResponse> findAvailableHotels(AvailabilityRequest request) {

        List<Room> rooms = roomRepository.findAvailableRooms(request.getCheckIn(),
                request.getCheckOut(),
                request.getAdult(),
                request.getKids());

        HashMap<Hotel,Integer> hotels = new HashMap<>();

        rooms.stream()
                .forEach(room -> {
                    Hotel hotel = room.getHotel();
                    if(!hotels.containsKey(hotel)){
                        hotels.put(hotel,1);
                    }else {
                        hotels.put(hotel,hotels.get(hotel).intValue()+1);
                    }
                        }
                        );

       return hotels.entrySet().stream()
               .map(hotelIntegerEntry -> {
                   Hotel hotel = hotelIntegerEntry.getKey();
                   return HotelResponse.builder()
                       .hotelId(hotel.getId())
                       .noOfRooms(hotelIntegerEntry.getValue())
                       .hotelName(hotel.getName())
                       .hotelServices(
                               mapHotelServicesToHotelServiceResponses(
                                       hotel.getHotelServices()))
                           .admin(hotel.getAdmin().getUsername())
                           .description(hotel.getDescription()).build();
               }).collect(Collectors.toSet());

    }

    private HotelResponse mapHotelToHotelResponse(Hotel hotel,Set<HotelService> hotelService){
        return HotelResponse.builder()
                .hotelId(hotel.getId())
                .admin(hotel.getAdmin().getEmail())
                .hotelName(hotel.getName())
                .description(hotel.getDescription())
                .hotelServices(mapHotelServicesToHotelServiceResponses(hotelService))
                .build();
    }

    private HotelResponse mapHotelToHotelResponse(Hotel hotel){
        return HotelResponse.builder()
                .hotelId(hotel.getId())
                .admin(hotel.getAdmin().getEmail())
                .hotelName(hotel.getName())
                .description(hotel.getDescription())
                .hotelServices(mapHotelServicesToHotelServiceResponses(hotel.getHotelServices()))
                .build();
    }

    private Set<HotelServiceResponse> mapHotelServicesToHotelServiceResponses(
            Set<HotelService> hotelServices
    ){
      return hotelServices.stream()
                .map(hotelService ->
                        HotelServiceResponse.builder()
                                .hotelServiceId(hotelService.getId())
                                .serviceName(hotelService.getService().getName())
                                .build())
                .collect(Collectors.toSet());
    }
}

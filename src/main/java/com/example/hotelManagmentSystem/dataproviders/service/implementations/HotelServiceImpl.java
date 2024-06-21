package com.example.hotelManagmentSystem.dataproviders.service.implementations;

import com.example.hotelManagmentSystem.core.exceptions.UploadImageException;
import com.example.hotelManagmentSystem.dataproviders.dto.request.AddHotelRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.request.AvailabilityRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.response.HotelResponse;
import com.example.hotelManagmentSystem.dataproviders.dto.response.HotelServiceResponse;
import com.example.hotelManagmentSystem.dataproviders.dto.response.ImageResponse;
import com.example.hotelManagmentSystem.dataproviders.entity.*;
import com.example.hotelManagmentSystem.dataproviders.repository.*;
import com.example.hotelManagmentSystem.dataproviders.service.interfaces.IHotelService;
import com.example.hotelManagmentSystem.dataproviders.entity.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class HotelServiceImpl implements IHotelService {
    private final HotelImageRepository hotelImageRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final HotelServiceRepository hotelServiceRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final String FOLDER_PATH = "C:\\Users\\USER\\OneDrive\\Desktop\\hotelManagmentSystem\\src\\main\\resources\\images\\";


//    @Override
//    public Set<HotelResponse> getAvailableHotelsFilterByRoomsAvailability(CheckAvailabilityRequest request) {
//
//    }

    @Override
    @Transactional
    public HotelResponse addHotel(AddHotelRequest addHotelRequest, String userEmail) {
        User user = userRepository.findUserByEmail(userEmail).get();

        Hotel hotelToAdd = Hotel
                .builder()
                .name(addHotelRequest.getHotelName())
                .admin(user)
                .description(addHotelRequest.getHotelDesc())
                .taxRate(addHotelRequest.getTaxRate())
                .build();
        log.info("Hotel built!");
        List<Service> services = serviceRepository.findAllById(addHotelRequest.getHotelServices());

        Set<HotelService> hotelServices = services.stream().map(
                service -> mapHotelServicesToHotelService(service, hotelToAdd)
        ).collect(Collectors.toSet());

        hotelRepository.save(hotelToAdd);

        hotelServiceRepository.saveAll(hotelServices);

        log.info("Hotel Service!");
        if ((addHotelRequest.getMultipartFiles()!=null)&&(!addHotelRequest.getMultipartFiles().isEmpty())){
            addHotelRequest.getMultipartFiles().stream()
                    .forEach(multipartFile ->
                    {
                        try {
                            uploadImagesToFileSystem(
                                    multipartFile,hotelToAdd
                            );
                        } catch (IOException e) {
                            throw new UploadImageException("Fotoja nuk u ngarkua!");
                        }
                    });
        }

        return mapHotelToHotelResponse(hotelToAdd, hotelServices);
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

        Set<Room> rooms = roomRepository.findAvailableRooms(request.getCheckIn(),
                request.getCheckOut(),
                request.getAdult(),
                request.getKids());

        HashMap<Hotel, Integer> hotels = new HashMap<>();

        rooms.stream()
                .forEach(room -> {
                            Hotel hotel = room.getHotel();
                            if (!hotels.containsKey(hotel)) {
                                hotels.put(hotel, 1);
                            } else {
                                hotels.put(hotel, hotels.get(hotel).intValue() + 1);
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
                            .description(hotel.getDescription())
                            .images(downloadImageFromFileSystem(hotel.getId())).build();
                }).collect(Collectors.toSet());

    }

    private HotelResponse mapHotelToHotelResponse(Hotel hotel, Set<HotelService> hotelService) {
        return HotelResponse.builder()
                .hotelId(hotel.getId())
                .admin(hotel.getAdmin().getEmail())
                .hotelName(hotel.getName())
                .description(hotel.getDescription())
                .hotelServices(mapHotelServicesToHotelServiceResponses(hotelService))
                .build();
    }

    private HotelResponse mapHotelToHotelResponse(Hotel hotel) {
        return HotelResponse.builder()
                .hotelId(hotel.getId())
                .admin(hotel.getAdmin().getEmail())
                .hotelName(hotel.getName())
                .description(hotel.getDescription())
                .noOfRooms(hotel.getRooms().size())
                .hotelServices(mapHotelServicesToHotelServiceResponses(hotel.getHotelServices()))
                .build();
    }

    private Set<HotelServiceResponse> mapHotelServicesToHotelServiceResponses(
            Set<HotelService> hotelServices
    ) {
        return hotelServices.stream()
                .map(hotelService ->
                        HotelServiceResponse.builder()
                                .hotelServiceId(hotelService.getId())
                                .serviceName(hotelService.getService().getName())
                                .build())
                .collect(Collectors.toSet());
    }

    private Set<ImageResponse> downloadImageFromFileSystem(Integer hotelId) {
        Set<ImageResponse> imageResponses = new HashSet<>();
        Set<HotelImage> hotelImages = hotelImageRepository.findByHotelId(hotelId);
        for (HotelImage fileData : hotelImages) {
            String filePath = fileData.getUrl();
            try {
                imageResponses.add(ImageResponse.builder()
                        .image(Files.readAllBytes(new File(filePath).toPath()))
                        .imageName(fileData.getName())
                        .message(fileData.getUrl()).build());
            } catch (IOException e) {
                imageResponses.add(ImageResponse.builder()
                        .image(null)
                        .imageName(fileData.getName())
                        .message("Imazhi me path: " + fileData.getUrl() +
                                "nuk mund te lexohej!").build());
            }
        }
        return imageResponses;
    }

    public String uploadImagesToFileSystem(MultipartFile multipartFile, Hotel hotel) throws IOException {

        String file_path = FOLDER_PATH + multipartFile.getOriginalFilename();

        //ruaj ne db pathin e file dhe type
        HotelImage hotelImage = hotelImageRepository
                .save(HotelImage.builder()
                        .name(multipartFile.getOriginalFilename())
                        .type(multipartFile.getContentType())
                        .url(file_path)
                        .hotel(hotel)
                        .build());

        //kalo filen ne filesystem
        multipartFile.transferTo(new File(file_path));

        if (hotelImage != null) {
            return "File upload succesfully: " + file_path;
        }
        return null;
    }
}

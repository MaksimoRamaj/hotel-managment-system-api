package com.example.hotelManagmentSystem.dataproviders.service.implementations;

import com.example.hotelManagmentSystem.core.exceptions.InvalidRequestException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
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
                service -> mapServicesToHotelService(service, hotelToAdd)
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


    private HotelService mapServicesToHotelService(Service service, Hotel hotelToAdd) {
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
    public Set<HotelResponse> findAvailableHotels(AvailabilityRequest request,
                                                  int pageNumber,
                                                  int pageSize) {

        if (request.getCheckIn().isBefore(LocalDate.now())){
            throw new InvalidRequestException("Booking not valid!");
        }
        if (request.getCheckIn().isAfter(request.getCheckOut())){
            throw new InvalidRequestException("Check-in should be before " +
                    "checkout!");
        }
        Pageable pageRequest = PageRequest.of(pageNumber,pageSize);
        Page<Object[]> hotelAndCount;
        if (request.getKids() <=0 && request.getAdult() <= 0) {
              hotelAndCount = hotelRepository.findAvailableHotels(request.getCheckIn(),
                request.getCheckOut(),
                      pageRequest);
        } else if (request.getKids() <= 0) {
              hotelAndCount = hotelRepository.findAvailableHotels(
                      request.getCheckIn(),
                      request.getCheckOut(),
                      request.getAdult(),
                      pageRequest);
        } else if (request.getAdult() <= 0) {
                hotelAndCount = hotelRepository.findAvailableHotelsWhenKidsPresent(
                        request.getCheckIn(),
                        request.getCheckOut(),
                        request.getKids(),
                        pageRequest);
        }else {
            hotelAndCount = hotelRepository.findAvailableHotels(
                    request.getCheckIn(),
                    request.getCheckOut(),
                    request.getKids(),
                    request.getAdult(),
                    pageRequest
            );
        }


       return hotelAndCount.get()
                .map(
                        object ->  mapHotelToHotelResponse((Hotel)object[0],((Long)object[1]).intValue(),hotelAndCount)
                ).collect(Collectors.toSet());
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

    private HotelResponse mapHotelToHotelResponse(Hotel hotel,int noOfRooms,Page page) {
        return HotelResponse.builder()
                .hotelId(hotel.getId())
                .admin(hotel.getAdmin().getEmail())
                .hotelName(hotel.getName())
                .description(hotel.getDescription())
                .noOfRooms(noOfRooms)
                .hotelServices(mapHotelServicesToHotelServiceResponses(hotel.getHotelServices()))
                .currentPage(page.getNumber())
                .totalPageNumber(page.getTotalPages())
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

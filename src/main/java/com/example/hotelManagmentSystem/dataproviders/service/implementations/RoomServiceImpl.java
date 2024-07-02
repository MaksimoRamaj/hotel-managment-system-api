package com.example.hotelManagmentSystem.dataproviders.service.implementations;

import com.example.hotelManagmentSystem.core.exceptions.InvalidRequestException;
import com.example.hotelManagmentSystem.core.exceptions.InvalidRoomPriceException;
import com.example.hotelManagmentSystem.core.exceptions.UploadImageException;
import com.example.hotelManagmentSystem.dataproviders.dto.request.AddRoomRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.request.AvailabilityRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.request.PriceDayDto;
import com.example.hotelManagmentSystem.dataproviders.dto.response.*;
import com.example.hotelManagmentSystem.dataproviders.entity.*;
import com.example.hotelManagmentSystem.dataproviders.repository.*;
import com.example.hotelManagmentSystem.dataproviders.service.interfaces.IRoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements IRoomService {
    private final RoomImageRepository roomImageRepository;
    private final RoomPriceRepository roomPriceRepository;

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;

    private final String FOLDER_PATH = "C:\\Users\\USER\\OneDrive\\Desktop\\hotelManagmentSystem\\src\\main\\resources\\images\\";


    @Override
    @Transactional
    public RoomResponse addRoom(AddRoomRequest request,String userEmail) {

        User user = userRepository.findUserByEmail(userEmail).get();
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(()->new InvalidRequestException("Hotel does not exists!"));

        if (!isPriceDayDtoValid(request.getPriceDayDto())){
            throw new InvalidRoomPriceException("Price should be > 0 or " +
                    "fill prices for all weekdays, no repeated days allowed!");
        };

        if (hotel.getAdmin().getId().intValue() != user.getId().intValue()){
            throw new InvalidRequestException("You should be the owner of the hotel to add the room!");
        }

//        if (request.getAdult() <= 0){
//            throw new InvalidRequestException("You should define the number of adults you expect in this room! > 0");
//        }

        Room room = Room.builder()
                .adult(request.getAdult() )
                .kids(request.getKids() > 0 ? request.getKids() : 0)
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
        if (!(request.getMultipartFiles() == null)){
            request.getMultipartFiles().stream()
                    .forEach(multipartFile ->
                    {
                        try {
                            uploadImagesToFileSystem(
                                    multipartFile,room
                            );
                        } catch (IOException e) {
                            throw new UploadImageException("Fotoja nuk u ngarkua!");
                        }
                    });
        }

        return mapToRoomResponse(room);
    }

    @Override
    public LinkedList<RoomOfHotelResponse> getRoomByHotelId(Integer hotelId, AvailabilityRequest request,
                                                     int pageNumber,int pageSize,String order) {

        if (request.getCheckIn().isBefore(LocalDate.now())){
            throw new InvalidRequestException("Booking not valid!");
        }
        if (request.getCheckIn().isAfter(request.getCheckOut())){
            throw new InvalidRequestException("Check-in should be before " +
                    "checkout!");
        }

        LinkedList<Object[]> rooms;

        if (order.equalsIgnoreCase("desc")){
            rooms = roomRepository.findAvailableRoomsByHotelIdAndDateRangeOrderDesc(
                    request.getCheckIn(),
                    request.getCheckOut().minusDays(1),
                    request.getKids() < 0 ? 0 : request.getKids(),
                    request.getAdult() < 0 ? 0 : request.getAdult(),
                    hotelId,
                    pageNumber,
                    pageSize
            );
        }else {
            rooms = roomRepository.findAvailableRoomsByHotelIdAndDateRange(
                    request.getCheckIn(),
                    request.getCheckOut().minusDays(1),
                    request.getKids() < 0 ? 0 : request.getKids(),
                    request.getAdult() < 0 ? 0 : request.getAdult(),
                    hotelId,
                    pageNumber,
                    pageSize
            );
        }


        return rooms.stream().map(objects ->
                RoomOfHotelResponse
                        .builder()
                        .roomId((Integer) objects[0])
                        .hotelId((Integer) objects[1])
                        .roomType((String) objects[2])
                        .adult((Integer)objects[3])
                        .kids((Integer)objects[4])
                        .description((String) objects[5])
                        .total((Double) objects[6])
                        .noOfDays(ChronoUnit.DAYS.between(request.getCheckIn(),request.getCheckOut())+1)
                        .imageResponse(downloadFirstImageFromFileSystem((Integer) objects[0]))
                        .build())
                .collect(Collectors.toCollection(LinkedList::new));

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
                .images(downloadImageFromFileSystem(room))
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

    public String uploadImagesToFileSystem(MultipartFile multipartFile, Room room) throws IOException {

        String file_path = FOLDER_PATH + multipartFile.getOriginalFilename();

        //ruaj ne db pathin e file dhe type
        RoomImage hotelImage = roomImageRepository
                .save(RoomImage.builder()
                        .name(multipartFile.getOriginalFilename())
                        .type(multipartFile.getContentType())
                        .url(file_path)
                        .room(room)
                        .build());

        //kalo filen ne filesystem
        multipartFile.transferTo(new File(file_path));

        if (hotelImage != null) {
            return "File upload succesfully: " + file_path;
        }
        return null;
    }

    private Set<ImageResponse> downloadImageFromFileSystem(Room roomId) {
        Set<ImageResponse> imageResponses = new HashSet<>();
        Set<RoomImage> roomImages = roomImageRepository.findByRoomId(roomId.getId());
        for (RoomImage fileData :roomImages) {
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

    private Set<ImageResponse> downloadImageFromFileSystem(Integer roomId) {
        Set<ImageResponse> imageResponses = new HashSet<>();
        Set<RoomImage> roomImages = roomImageRepository.findByRoomId(roomId);
        for (RoomImage fileData :roomImages) {
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

    private ImageResponse downloadFirstImageFromFileSystem(Integer roomId) {
        ImageResponse imageResponse;
        RoomImage roomImages = roomImageRepository.findFirstByRoomId(roomId);
        String filePath;

        if (roomImages == null){
                    filePath = "src/main/resources/images/dummyPicture.png";
                try {
                    imageResponse = ImageResponse.builder()
                            .image(Files.readAllBytes(new File(filePath).toPath()))
                            .imageName("dummyPicture!")
                            .message("dummyPicture.jpg").build();
                } catch (IOException e) {
                    imageResponse = ImageResponse.builder()
                            .image(null)
                            .imageName("dummyPicture")
                            .message("Imazhi " +
                                    "nuk mund te lexohej!").build();
                }
        }else{
                filePath = roomImages.getUrl();
                try {
                    imageResponse = ImageResponse.builder()
                            .image(Files.readAllBytes(new File(filePath).toPath()))
                            .imageName(roomImages.getName())
                            .message(roomImages.getUrl()).build();
                } catch (IOException e) {
                    imageResponse = ImageResponse.builder()
                            .image(null)
                            .imageName(roomImages.getName())
                            .message("Imazhi me path: " + roomImages.getUrl() +
                                    "nuk mund te lexohej!").build();
                }
        }
            return imageResponse;
        }

}



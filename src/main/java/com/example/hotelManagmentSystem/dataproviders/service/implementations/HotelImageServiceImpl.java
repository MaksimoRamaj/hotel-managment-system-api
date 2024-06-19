package com.example.hotelManagmentSystem.dataproviders.service.implementations;

import com.example.hotelManagmentSystem.dataproviders.entity.Hotel;
import com.example.hotelManagmentSystem.dataproviders.entity.HotelImage;
import com.example.hotelManagmentSystem.dataproviders.repository.HotelImageRepository;
import com.example.hotelManagmentSystem.dataproviders.repository.HotelRepository;
import com.example.hotelManagmentSystem.dataproviders.service.interfaces.IHotelImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelImageServiceImpl implements IHotelImageService {

    private final HotelImageRepository hotelImageRepository;
    private final HotelRepository hotelRepository;
    private final String FOLDER_PATH = "C:\\Users\\USER\\OneDrive\\Desktop\\hotelManagmentSystem\\src\\main\\resources\\images\\";

    public String uploadImageToFileSystem(MultipartFile multipartFile,
                                          Integer hotelId) throws IOException {

        Hotel hotel = hotelRepository.findById(hotelId).get();

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

        if (hotelImage!=null){
            return "File upload succesfully: " + file_path;
        }
        return null;
    }

    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<HotelImage> fileData = hotelImageRepository.findByName(fileName);
        String filePath=fileData.get().getUrl();
        byte[] image = Files.readAllBytes(new File(filePath).toPath());
        return image;
    }

}

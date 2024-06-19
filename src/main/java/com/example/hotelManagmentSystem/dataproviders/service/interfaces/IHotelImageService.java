package com.example.hotelManagmentSystem.dataproviders.service.interfaces;

import com.example.hotelManagmentSystem.dataproviders.entity.Hotel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IHotelImageService {
     byte[] downloadImageFromFileSystem(String fileName) throws IOException;
     String uploadImageToFileSystem(MultipartFile multipartFile, Integer hotel) throws IOException;
}

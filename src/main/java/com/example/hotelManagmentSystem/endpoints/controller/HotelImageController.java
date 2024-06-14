package com.example.hotelManagmentSystem.endpoints.controller;


import com.example.hotelManagmentSystem.dataproviders.service.interfaces.IHotelImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/image")
@RequiredArgsConstructor
public class HotelImageController {

    private final IHotelImageService service;
//
//    @PostMapping("/fileSystem")
//    public ResponseEntity<?> uploadImageToFIleSystem(@RequestParam("image") MultipartFile file) throws IOException, IOException {
//        String uploadImage = service.uploadImageToFileSystem(file);
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(uploadImage);
//    }

    @GetMapping("/fileSystem/{fileName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String fileName) throws IOException {
        byte[] imageData=service.downloadImageFromFileSystem(fileName);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }
}


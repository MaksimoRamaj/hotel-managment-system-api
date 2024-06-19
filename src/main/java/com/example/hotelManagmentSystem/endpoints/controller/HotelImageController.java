package com.example.hotelManagmentSystem.endpoints.controller;

import com.example.hotelManagmentSystem.dataproviders.service.interfaces.IHotelImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/image")
@RequiredArgsConstructor
public class HotelImageController {


    private final IHotelImageService hotelImageService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/fileSystem/hotel/{hotelId}")
    public ResponseEntity<?> uploadImageToFIleSystem(@PathVariable Integer hotelId,
                                                     @RequestParam("image") MultipartFile file) throws IOException {
        String uploadImage = hotelImageService.uploadImageToFileSystem(file, hotelId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    @GetMapping("/fileSystem/{fileName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String fileName) throws IOException, IOException {
        byte[] imageData = hotelImageService.downloadImageFromFileSystem(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);

    }
}
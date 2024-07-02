package com.example.hotelManagmentSystem.endpoints.controller;

import com.example.hotelManagmentSystem.dataproviders.dto.request.AddHotelRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.request.AvailabilityRequest;
import com.example.hotelManagmentSystem.dataproviders.repository.HotelRepository;
import com.example.hotelManagmentSystem.dataproviders.service.implementations.JwtService;
import com.example.hotelManagmentSystem.dataproviders.service.interfaces.IHotelService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/hotel")
@RequiredArgsConstructor
public class HotelController {

    private final IHotelService hotelService;
    private final JwtService jwtService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addHotel(@ModelAttribute AddHotelRequest request,
                                      @NonNull HttpServletRequest httpServletRequest){
            String authHeader = httpServletRequest.getHeader("Authorization");
        String jwtToken = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwtToken);
        return new ResponseEntity<>(hotelService.addHotel(request,userEmail), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/all")
    public ResponseEntity<?> findAll(){
        return new ResponseEntity<>(
                hotelService.findAll(),
                HttpStatus.OK
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all-by-user")
    public ResponseEntity<?> getAllByUserId(@NonNull HttpServletRequest httpServletRequest){
        String authHeader = httpServletRequest.getHeader("Authorization");
        String jwtToken = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwtToken);
        return new ResponseEntity<>(hotelService
                .findAllHotelsByUser(userEmail),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/available")
    public ResponseEntity<?> checkAvailability(
            @RequestBody AvailabilityRequest request,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "6") int pageSize
    ){
        return new ResponseEntity<>(
                hotelService.findAvailableHotels(request,pageNumber,pageSize),
                HttpStatus.ACCEPTED
        );
    }

}

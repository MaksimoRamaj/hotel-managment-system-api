package com.example.hotelManagmentSystem.endpoints.controller;

import com.example.hotelManagmentSystem.dataproviders.dto.request.AddHotelRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.request.AvailabilityRequest;
import com.example.hotelManagmentSystem.dataproviders.service.implementations.JwtService;
import com.example.hotelManagmentSystem.dataproviders.service.interfaces.IHotelService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/hotel")
@RequiredArgsConstructor
public class HotelController {

    private final IHotelService hotelService;
    private final JwtService jwtService;
    @PostMapping("/add")
    public ResponseEntity<?> addHotel(@RequestBody AddHotelRequest request,
                                      @NonNull HttpServletRequest httpServletRequest){
        String authHeader = httpServletRequest.getHeader("Authorization");
        String jwtToken = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwtToken);
        return new ResponseEntity<>(hotelService.addHotel(request,userEmail), HttpStatus.ACCEPTED);
    }


    @GetMapping("/all")
    public ResponseEntity<?> findAll(){
        return new ResponseEntity<>(
                hotelService.findAll(),
                HttpStatus.OK
        );
    }

    @GetMapping("/all-by-user")
    public ResponseEntity<?> getAllByUserId(@NonNull HttpServletRequest httpServletRequest){
        String authHeader = httpServletRequest.getHeader("Authorization");
        String jwtToken = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwtToken);
        return new ResponseEntity<>(hotelService
                .findAllHotelsByUser(userEmail),HttpStatus.OK);
    }

    @PostMapping("/available")
    public ResponseEntity<?> checkAvailability(
            @RequestBody AvailabilityRequest request
    ){
        return new ResponseEntity<>(
                hotelService.findAvailableHotels(request),
                HttpStatus.ACCEPTED
        );
    }

}

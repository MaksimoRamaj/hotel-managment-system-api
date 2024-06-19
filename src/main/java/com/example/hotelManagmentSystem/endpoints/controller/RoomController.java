package com.example.hotelManagmentSystem.endpoints.controller;

import com.example.hotelManagmentSystem.dataproviders.dto.request.AddRoomRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.request.AvailabilityRequest;
import com.example.hotelManagmentSystem.dataproviders.service.implementations.JwtService;
import com.example.hotelManagmentSystem.dataproviders.service.interfaces.IRoomService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {

    private final IRoomService roomService;
    private final JwtService jwtService;

    @PostMapping("/add")
    public ResponseEntity<?> addRoom(@RequestBody AddRoomRequest request,@NonNull HttpServletRequest httpServletRequest){
        String authHeader = httpServletRequest.getHeader("Authorization");
        String jwtToken = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwtToken);
        return new ResponseEntity<>(
                roomService.addRoom(request,userEmail)
                ,HttpStatus.ACCEPTED);
    }

    @PostMapping("/available/{hotelId}")
    public ResponseEntity<?> getRoomByHotelId(@PathVariable Integer hotelId,
                                              @RequestBody AvailabilityRequest request){
        return new ResponseEntity<>(
                roomService.getRoomByHotelId(hotelId,request),
                HttpStatus.OK
        );
    }

//    @PostMapping("/available")
//    public ResponseEntity<?> checkAvailability(
//            @RequestBody AvailabilityRequest request
//            ){
//        return new ResponseEntity<>(
//                roomService.findAvailableRooms(request),
//                HttpStatus.ACCEPTED
//        );
//    }

//    @PostMapping("/reserve")
//    public ResponseEntity<?> reserveRoom(@RequestBody ReservationRequest request,
//                                         @NonNull HttpServletRequest httpServletRequest){
//        String authHeader = httpServletRequest.getHeader("Authorization");
//        String jwtToken = authHeader.substring(7);
//        String userEmail = jwtService.extractUsername(jwtToken);
//
//        return new ResponseEntity<>(
//                roomService.reserveRoom(request,userEmail),HttpStatus.ACCEPTED
//        );
//    }


}

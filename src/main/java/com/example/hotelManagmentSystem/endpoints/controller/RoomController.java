package com.example.hotelManagmentSystem.endpoints.controller;

import com.example.hotelManagmentSystem.dataproviders.dto.request.AddRoomRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.request.AvailabilityRequest;
import com.example.hotelManagmentSystem.dataproviders.service.implementations.JwtService;
import com.example.hotelManagmentSystem.dataproviders.service.interfaces.IRoomService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {

    private final IRoomService roomService;
    private final JwtService jwtService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/add" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE
            ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addRoom(@ModelAttribute AddRoomRequest request,@NonNull HttpServletRequest httpServletRequest){
        String authHeader = httpServletRequest.getHeader("Authorization");
        String jwtToken = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwtToken);
        return new ResponseEntity<>(
                roomService.addRoom(request,userEmail)
                ,HttpStatus.ACCEPTED);
    }


    @PostMapping("/available/{hotelId}")
    public ResponseEntity<?> getRoomByHotelId(@PathVariable Integer hotelId,
                                              @RequestBody AvailabilityRequest request,
                                              @RequestParam int pageNumber,
                                              @RequestParam int pageSize,
                                              @RequestParam(defaultValue = "asc") String order){
        return new ResponseEntity<>(
                roomService.getRoomByHotelId(hotelId,request,pageNumber,pageSize,order),
                HttpStatus.OK
        );
    }



}

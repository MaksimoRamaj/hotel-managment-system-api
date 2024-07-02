package com.example.hotelManagmentSystem.endpoints.controller;

import com.example.hotelManagmentSystem.dataproviders.dto.request.BookRequest;
import com.example.hotelManagmentSystem.dataproviders.service.implementations.JwtService;
import com.example.hotelManagmentSystem.dataproviders.service.interfaces.IReservationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final JwtService jwtService;
    private final IReservationService reservationService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<?> book(@Valid @RequestBody BookRequest request,
                                  @NonNull HttpServletRequest httpServletRequest){
        String authHeader = httpServletRequest.getHeader("Authorization");
        String jwtToken = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwtToken);

        return new ResponseEntity<>(
                reservationService.book(request,userEmail), HttpStatus.ACCEPTED
        );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/history")
    public ResponseEntity<?> getAllUserReservations(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @NonNull HttpServletRequest httpServletRequest){
        String authHeader = httpServletRequest.getHeader("Authorization");
        String jwtToken = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwtToken);

        return new ResponseEntity<>(
                reservationService.getReservationsByUser(userEmail,pageNumber,pageSize),
                HttpStatus.OK
        );
    }
}

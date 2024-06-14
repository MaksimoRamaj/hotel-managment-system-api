package com.example.hotelManagmentSystem.dataproviders.service.implementations;


import com.example.hotelManagmentSystem.dataproviders.dto.request.AuthenticationRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.request.RegisterRequest;
import com.example.hotelManagmentSystem.dataproviders.dto.response.AuthenticationResponse;
import com.example.hotelManagmentSystem.dataproviders.dto.response.ClientAuthResponse;
import com.example.hotelManagmentSystem.dataproviders.entity.*;
import com.example.hotelManagmentSystem.dataproviders.repository.ClientLogRepository;
import com.example.hotelManagmentSystem.dataproviders.repository.RoleRepository;
import com.example.hotelManagmentSystem.dataproviders.repository.UserRepository;
import com.example.hotelManagmentSystem.dataproviders.service.interfaces.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final AddressRepository addressRepository;
    private final ClientLogRepository clientLogRepository;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<?> authenticate(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findUserByEmail(authentication.getName()).
                orElseThrow(() -> new UsernameNotFoundException("User not found"));
        log.info("authenticate - UserService: " + user.getEmail() + " | " + user.getPassword() + " | " + user.getRole().getName());
        List<String> rolesNames = new ArrayList<>();
        rolesNames.add(user.getRole().getName());
        String token = jwtService.generateToken(user,rolesNames);
        if (user.getRole().getName().equalsIgnoreCase("USER")){
            return new ResponseEntity<>(new ClientAuthResponse(
                    token,
                    user.getRole().getName(),
                    user.getClientLog().getTotalReservations(),
                    user.getClientLog().getScore(),
                    user.getFirstname(),
                    user.getLastname(),
                    user.getAddress()
            ),HttpStatus.OK);
        }
        return new ResponseEntity<>(new AuthenticationResponse(token,user.getRole().getName()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())){
            return new ResponseEntity<>("Email is taken!", HttpStatus.CONFLICT);
        }else {
            User user = new User();
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.
                    getPassword()));
            String myrole = "user";
            log.info("register request role: " + registerRequest.getRole());
            if (registerRequest.getRole().isEmpty() || registerRequest.
                    getRole().equalsIgnoreCase("user")) {
                myrole = "USER";
            }

            if (registerRequest.getRole().equalsIgnoreCase("admin")) {
                myrole = "ADMIN";
            }
            Role role = roleRepository.findByName(myrole).get();
            log.info("Role from repo: " + role.getName());

            Address address = Address.builder()
                            .state(registerRequest.getAddress().getState())
                                    .city(registerRequest.getAddress().getCity())
                                            .zipCode(registerRequest.getAddress().getZipCode())
                                                    .street(registerRequest.getAddress().getStreet())
                                                            .build();

            addressRepository.save(address);

            user.setRole(role);
            user.setUsername(registerRequest.getUsername());
            user.setFirstname(registerRequest.getFirstname());
            user.setLastname(registerRequest.getLastname());
            user.setAddress(address);

            userRepository.save(user);

            if (user.getRole().getName().equalsIgnoreCase("USER")){
                clientLogRepository.save(ClientLog.builder()
                        .client(user)
                        .score(0.0)
                        .totalReservations(0)
                        .build());
            }


            //useri ne response test case
            String token = jwtService.generateToken(user,
                    Collections.singletonList(role.getName()));
            return new ResponseEntity<>(new AuthenticationResponse(token,user.getRole().getName()),HttpStatus.OK);
        }
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}

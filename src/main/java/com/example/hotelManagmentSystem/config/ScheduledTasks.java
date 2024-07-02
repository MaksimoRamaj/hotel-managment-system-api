package com.example.hotelManagmentSystem.config;

import com.example.hotelManagmentSystem.dataproviders.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    //fshi cdo dite te 15 te muajit ne mesnate te gjithe token nga db

    private final TokenRepository tokenRepository;

    @Scheduled(cron = "0 0 0 15 * ?")
    public void deleteTokens() {
        tokenRepository.deleteAll();
    }
}

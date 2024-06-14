package com.example.hotelManagmentSystem.dataproviders.repository;

import com.example.hotelManagmentSystem.dataproviders.entity.ClientLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientLogRepository extends JpaRepository<ClientLog, Integer> {
}
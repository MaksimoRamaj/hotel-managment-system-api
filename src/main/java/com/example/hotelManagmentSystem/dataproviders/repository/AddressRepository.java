package com.example.hotelManagmentSystem.dataproviders.repository;

import com.example.hotelManagmentSystem.dataproviders.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
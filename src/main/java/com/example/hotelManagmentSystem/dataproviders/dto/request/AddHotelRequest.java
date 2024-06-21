package com.example.hotelManagmentSystem.dataproviders.dto.request;

import com.example.hotelManagmentSystem.dataproviders.entity.HotelService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class AddHotelRequest {
    private String hotelName;
    private String hotelDesc;
    private Double taxRate;
    List<Integer> hotelServices;
    private Set<MultipartFile> multipartFiles;
}

package com.example.hotelManagmentSystem.dataproviders.dto.request;

import com.example.hotelManagmentSystem.dataproviders.entity.HotelService;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "hotelName field must not be blank!")
    private String hotelName;
    @NotBlank(message = "hotelDesc field must not be blank!")
    private String hotelDesc;
    private Double taxRate;
    Set<Integer> hotelServices;
    private Set<MultipartFile> multipartFiles;
}

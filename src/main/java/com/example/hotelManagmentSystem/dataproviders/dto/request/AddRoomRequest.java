package com.example.hotelManagmentSystem.dataproviders.dto.request;

import com.example.hotelManagmentSystem.dataproviders.entity.RoomType;
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
public class AddRoomRequest {
    @NotBlank(message = "Room type field must not be blank!")
    private String roomType;
    private Set<PriceDayDto> priceDayDto;
    @NotBlank(message = "Description field must not be blank!")
    private String description;
    private int adult;
    private int kids;
    private Integer hotelId;
    Set<MultipartFile> multipartFiles;
}

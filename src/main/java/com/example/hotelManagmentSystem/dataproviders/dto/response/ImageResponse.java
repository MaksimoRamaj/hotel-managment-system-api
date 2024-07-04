package com.example.hotelManagmentSystem.dataproviders.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class ImageResponse {
    private String image;
    private String imageName;
    private String message;
}

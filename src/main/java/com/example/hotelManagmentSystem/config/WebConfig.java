package com.example.hotelManagmentSystem.config;

import com.example.hotelManagmentSystem.converters.StringToHotelServicesConverter;
import com.example.hotelManagmentSystem.converters.StringToPriceDayDtoSetConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final StringToPriceDayDtoSetConverter stringToPriceDayDtoSetConverter;
    private final StringToHotelServicesConverter stringToHotelServicesConverter;
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToPriceDayDtoSetConverter);
        registry.addConverter(stringToHotelServicesConverter);
    }

}

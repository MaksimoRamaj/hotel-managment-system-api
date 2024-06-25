package com.example.hotelManagmentSystem.config;

import com.example.hotelManagmentSystem.converters.ArrayOfStringToSetInteger;
import com.example.hotelManagmentSystem.converters.StringToHotelServicesConverter;
import com.example.hotelManagmentSystem.converters.StringToPriceDayDtoSetConverter;
import com.example.hotelManagmentSystem.converters.StringToSetOfMultipartFileConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final StringToPriceDayDtoSetConverter stringToPriceDayDtoSetConverter;
    private final StringToHotelServicesConverter stringToHotelServicesConverter;
    private final com.example.hotelManagmentSystem.config.StringToSetIntegerConverter stringToSetIntegerConverter;
    private final StringToSetOfMultipartFileConverter stringToSetOfMultipartFileConverter;
    private final ArrayOfStringToSetInteger arrayOfStringToSetInteger;


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToPriceDayDtoSetConverter);
        registry.addConverter(stringToHotelServicesConverter);
        registry.addConverter(stringToSetIntegerConverter);
        registry.addConverter(stringToSetOfMultipartFileConverter);
        registry.addConverter(arrayOfStringToSetInteger);
    }

}

package com.example.hotelManagmentSystem.converters;

import com.example.hotelManagmentSystem.dataproviders.dto.request.PriceDayDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class StringToPriceDayDtoSetConverter implements Converter<String, Set<PriceDayDto>> {

    private final ObjectMapper objectMapper;

    @Override
    public Set<PriceDayDto> convert(String source) {
        try {
            PriceDayDto[] priceDayDtos = objectMapper.readValue(source, PriceDayDto[].class);
            Set<PriceDayDto> set = new HashSet<>();
            for (PriceDayDto priceDayDto : priceDayDtos) {
                set.add(priceDayDto);
            }
            return set;
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid format for PriceDayDto", e);
        }
    }
}

package com.example.hotelManagmentSystem.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class StringToHotelServicesConverter implements Converter<String, Set<Integer>> {

    private final ObjectMapper objectMapper;

    @Override
    public Set<Integer> convert(String source) {
        try{
            Integer[] integers = objectMapper.readValue(source, Integer[].class);
            Set<Integer> integerSet = new HashSet<>();
            integerSet.addAll(Arrays.asList(integers));
            return integerSet;
        }catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid format for HotelServices", e);
        }
    }
}

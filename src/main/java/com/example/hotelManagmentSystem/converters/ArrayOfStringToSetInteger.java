package com.example.hotelManagmentSystem.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ArrayOfStringToSetInteger implements Converter<String, Set<Integer>> {

    @Override
    public Set<Integer> convert(String source) {
        return Arrays.stream(source.replaceAll("[\\[\\]]", "").split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }
}
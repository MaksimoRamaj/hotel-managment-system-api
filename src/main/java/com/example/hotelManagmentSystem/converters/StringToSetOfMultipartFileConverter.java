package com.example.hotelManagmentSystem.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class StringToSetOfMultipartFileConverter implements Converter<String, Set<MultipartFile>> {

    private final ObjectMapper objectMapper;

    @Override
    public Set<MultipartFile> convert(String source) {
        try {
            // Remove square brackets and split by comma
            String[] sourceArray = source.replaceAll("[\\[\\]]", "").split(",");
            Set<MultipartFile> multipartFiles = new HashSet<>();

            // Process each string entry
            for (String fileString : sourceArray) {
                MultipartFile multipartFile = objectMapper.readValue(fileString.trim(), MultipartFile.class);
                multipartFiles.add(multipartFile);
            }
            return multipartFiles;
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid format for MultipartFile", e);
        }
    }
}

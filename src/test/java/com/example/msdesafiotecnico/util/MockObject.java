package com.example.msdesafiotecnico.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class MockObject {

    public static <T> T getObjectFromFile(String path, Class<T> objectClass) throws IOException {
        File file = new ClassPathResource(path).getFile();
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        return objectMapper.readValue(file, objectClass);
    }

    public static <T> List<T> getListFromFile(String path, Class<T> objectClass) throws IOException {
        File file = new ClassPathResource(path).getFile();
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, objectClass);
        return objectMapper.readValue(file, collectionType);
    }
}

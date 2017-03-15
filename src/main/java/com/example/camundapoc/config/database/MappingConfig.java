package com.example.camundapoc.config.database;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by spencer on 05/08/2016.
 */
@Configuration
public class MappingConfig {

    private static ObjectMapper objectMapper;

    public static ObjectMapper getObjectMapper() {
        if( objectMapper == null ) {
            objectMapper = createObjectMapper();
        }
        return objectMapper;
    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder()
    {
        Jackson2ObjectMapperBuilder mapperBuilder = new Jackson2ObjectMapperBuilder();
        Jackson2ObjectMapperBuilder.json().configure(getObjectMapper());
        return mapperBuilder;
    }


    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return getObjectMapper();
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        objectMapper.setDateFormat(df);
        // Uses Enum.toString() for serialization of an Enum
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        // Uses Enum.toString() for deserialization of an Enum
        objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);


        return objectMapper;

    }

}

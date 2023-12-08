package com.coco3x.jnu_course_seat_alert.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomJson {
    public static ObjectMapper objectMapper = new ObjectMapper();
    public static <T> T convertJsonString2Object(String jsonString, Class<T> clazz) throws JsonProcessingException{
        return objectMapper.readValue(jsonString, clazz);
    }
    public static String convertObject2JsonString(Object objectToBeConverted) throws JsonProcessingException{
        return objectMapper.writeValueAsString(objectToBeConverted);
    }
}

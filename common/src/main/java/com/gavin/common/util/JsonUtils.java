package com.gavin.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Slf4j
public class JsonUtils {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 指定对于Date类型的序列化方式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        objectMapper.setDateFormat(dateFormat);
    }

    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("failed to convert object{} to json.", obj);
            return null;
        }
    }

    public static Object parseText(String json, Class clazz) throws IOException {
        return objectMapper.readValue(json, clazz);
    }

}

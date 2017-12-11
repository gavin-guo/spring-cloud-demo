package com.gavin.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class JsonUtils {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 指定对于Date类型的序列化方式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        objectMapper.setDateFormat(dateFormat);
    }

    public static String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    public static Object parseText(String json, Class clazz) throws IOException {
        return objectMapper.readValue(json, clazz);
    }

}

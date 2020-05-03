package com.shl.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.compress.utils.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public JsonUtils() {
    }

    public static String writeValueAsString(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception var2) {
            return null;
        }
    }

    public static Map<String, Object> readValue(String content) {
        try {
            return (Map) objectMapper.readValue(content, Map.class);
        } catch (Exception var2) {

            return new HashMap<>();
        }
    }

    public static List readListValue(String content) {
        try {
            return (List) objectMapper.readValue(content, List.class);
        } catch (Exception var2) {

            return Lists.newArrayList();
        }
    }

    public static <T> T readValueByClass(String content, Class<T> clazz) {
        try {
            return objectMapper.readValue(content, clazz);
        } catch (Exception var3) {

            return null;
        }
    }
}

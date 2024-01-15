package com.moraes.authenticator.api.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonObjectUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonObjectUtil() {
    }

    public static Map<String, Object> convertObjectToMap(Object object) {
        if (object != null) {
            return OBJECT_MAPPER.convertValue(object, new TypeReference<Map<String, Object>>() {
            });
        }
        return null;
    }

    public static <T> List<T> convertMapsToListOfSomething(List<Map<String, Object>> object, Class<T> targetType) {
        if (object != null) {
            List<T> resultList = new LinkedList<>();
            for (Map<String, Object> map : object) {
                T convertedObject = OBJECT_MAPPER.convertValue(map, targetType);
                resultList.add(convertedObject);
            }
            return resultList;
        }
        return null;
    }

}

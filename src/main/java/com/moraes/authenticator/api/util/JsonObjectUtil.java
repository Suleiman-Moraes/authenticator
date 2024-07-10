package com.moraes.authenticator.api.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JsonObjectUtil {

	private static final ObjectMapper OBJECT_MAPPER = init();

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

	private static ObjectMapper init() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		return mapper;
	}
}

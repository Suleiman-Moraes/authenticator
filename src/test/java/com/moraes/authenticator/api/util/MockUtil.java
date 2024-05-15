package com.moraes.authenticator.api.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public final class MockUtil {

    private MockUtil() {
        /** empty */
    }

    public static void toFill(Object object, Integer number, List<String> ignoreFields) throws Exception {
        for (Field field : object.getClass().getDeclaredFields()) {
            if (!ignoreFields.contains(field.getName())) {
                setValue(object, field.getName(), field.getType(), MockUtil.generateValue(field, number));
            }
        }
    }

    public static void setValue(Object entity, String name, Class<?> type, Object value) throws Exception {
        name = String.format("set%s", name.replaceFirst("^.", ("" + name.charAt(0)).toUpperCase()));
        entity.getClass().getMethod(name, type).invoke(entity, value);
    }

    public static String getStringBySize(Integer size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append("a");
        }
        return sb.toString();
    }

    public static Object generateValue(Field field, Integer number) {
        switch (field.getType().getSimpleName().toUpperCase()) {
            case "BOOLEAN":
                return true;
            case "DOUBLE":
                return number.doubleValue();
            case "LONG":
                return number.longValue();
            case "INTEGER":
            case "INT":
                return number;
            case "BIGDECIMAL":
                return BigDecimal.ONE;
            case "DATE":
                return new Date();
            case "LOCALDATETIME":
                return LocalDateTime.now();
            case "LOCALDATE":
                return LocalDate.now();
            case "STRING":
                return "Teste String" + number;
            default:
                return null;
        }
    }
}

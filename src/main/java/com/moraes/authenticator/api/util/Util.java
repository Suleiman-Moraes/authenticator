package com.moraes.authenticator.api.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;

public final class Util {

    private Util() {

    }

    public static boolean isNotEmptyAndGreaterThanZero(Number number) {
        return number != null && number.doubleValue() > 0;
    }

    public static LocalDate calendarToLocalDate(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        // Obter o Instant a partir do Calendar
        Instant instant = calendar.toInstant();

        // Definir o ZoneId
        ZoneId zoneId = ZoneId.systemDefault();

        // Converter Instant para LocalDate
        return instant.atZone(zoneId).toLocalDate();
    }
}

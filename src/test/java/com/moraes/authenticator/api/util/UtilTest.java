package com.moraes.authenticator.api.util;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Calendar;

import org.junit.jupiter.api.Test;

class UtilTest {

    @Test
    void testIsNotEmptyAndGreaterThanZero() {
        final Long numLong = 1L;
        final Long numLongN = -1L;
        final Integer numInteger = 1;
        final Integer numIntegerN = -1;
        final Double numDouble = 1.0;
        final Double numDoubleN = -1.0;
        final Float numFloat = 1.0F;
        final Float numFloatN = -1.0F;
        assertTrue(Util.isNotEmptyAndGreaterThanZero(numLong), "Return not equal");
        assertFalse(Util.isNotEmptyAndGreaterThanZero(numLongN), "Return not equal");
        assertTrue(Util.isNotEmptyAndGreaterThanZero(numInteger), "Return not equal");
        assertFalse(Util.isNotEmptyAndGreaterThanZero(numIntegerN), "Return not equal");
        assertTrue(Util.isNotEmptyAndGreaterThanZero(numDouble), "Return not equal");
        assertFalse(Util.isNotEmptyAndGreaterThanZero(numDoubleN), "Return not equal");
        assertTrue(Util.isNotEmptyAndGreaterThanZero(numFloat), "Return not equal");
        assertFalse(Util.isNotEmptyAndGreaterThanZero(numFloatN), "Return not equal");
        assertFalse(Util.isNotEmptyAndGreaterThanZero(null), "Return not equal");
    }

    @Test
    void testCalendarToLocalDate_WithSpecificDate() {
        // Arrange
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.MAY, 21);

        // Act
        LocalDate result = Util.calendarToLocalDate(calendar);

        // Assert
        assertNotNull(result);
        assertEquals(LocalDate.of(2023, 5, 21), result);
    }

    @Test
    void testCalendarToLocalDate_WithNullCalendar() {
        // Act
        LocalDate result = Util.calendarToLocalDate(null);

        // Assert
        assertNull(result);
    }

    @Test
    void testCalendarToLocalDate_WithCurrentDate() {
        // Arrange
        Calendar calendar = Calendar.getInstance();
        LocalDate expectedDate = LocalDate.now();

        // Act
        LocalDate result = Util.calendarToLocalDate(calendar);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDate, result);
    }

    @Test
    void testCalendarToLocalDate_LeapYear() {
        // Arrange
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, Calendar.FEBRUARY, 29); // Leap year date

        // Act
        LocalDate result = Util.calendarToLocalDate(calendar);

        // Assert
        assertNotNull(result);
        assertEquals(LocalDate.of(2020, 2, 29), result);
    }

    @Test
    void testCalendarToLocalDate_DifferentTimeZones() {
        // Arrange
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.DECEMBER, 31, 23, 59, 59); // Setting date and time

        // Act
        LocalDate result = Util.calendarToLocalDate(calendar);

        // Assert
        assertNotNull(result);
        assertEquals(LocalDate.of(2023, 12, 31), result);
    }
}

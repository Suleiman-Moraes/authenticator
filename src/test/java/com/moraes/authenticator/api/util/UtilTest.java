package com.moraes.authenticator.api.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}

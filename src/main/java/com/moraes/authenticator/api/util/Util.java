package com.moraes.authenticator.api.util;

public final class Util {

    private Util() {

    }

    public static boolean isNotEmptyAndGreaterThanZero(Number number) {
        return number != null && number.doubleValue() > 0;
    }
}

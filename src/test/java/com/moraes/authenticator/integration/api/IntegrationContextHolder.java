package com.moraes.authenticator.integration.api;

public class IntegrationContextHolder {

    public static final String BASIC_TOKEN = "Basic d2ViOjEyMzQ1Ng==";
    public static final String USERNAME = "admin";
    public static String USERNAME_ME = "userMe1";
    public static String PASSWORD_ME = "1234567";

    public static String ACCESS_TOKEN = "";
    public static String ACCESS_TOKEN_ME = "";

    private IntegrationContextHolder() {
    }

    public static void setAccessToken(String accessToken) {
        ACCESS_TOKEN = String.format("Bearer %s", accessToken);
    }

    public static void setAccessTokenMe(String accessTokenMe) {
        ACCESS_TOKEN_ME = String.format("Bearer %s", accessTokenMe);
    }
}

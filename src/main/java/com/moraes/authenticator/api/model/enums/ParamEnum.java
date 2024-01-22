package com.moraes.authenticator.api.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ParamEnum {

    TOKEN_RESET_PASSWORD_EXPIRATION_TIME("2", "Expiration time for Token Reset Password in hours."),
    URL_FRONT_END("http://localhost:4200", "URL Front End.");

    private final String defaultValue;

    private final String description;
}

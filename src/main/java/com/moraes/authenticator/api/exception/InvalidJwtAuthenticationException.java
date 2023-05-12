package com.moraes.authenticator.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.moraes.authenticator.api.util.MessagesUtil;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidJwtAuthenticationException extends PatternException {

    public InvalidJwtAuthenticationException() {
        this(MessagesUtil.getMessage(MessagesUtil.RESOURCE_NOT_FOUND));
    }

    public InvalidJwtAuthenticationException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}

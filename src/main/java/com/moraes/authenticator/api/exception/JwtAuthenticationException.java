package com.moraes.authenticator.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.moraes.authenticator.api.util.MessagesUtil;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(String msg) {
        super(msg);
    }

    public JwtAuthenticationException() {
        this(MessagesUtil.getMessage("expire_or_invalid_token"));
    }
}

package com.moraes.authenticator.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.moraes.authenticator.api.util.MessagesUtil;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserInactiveException extends PatternException {

    public UserInactiveException() {
        super(MessagesUtil.getMessage("user_inactive"), HttpStatus.FORBIDDEN);
    }

    public UserInactiveException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}

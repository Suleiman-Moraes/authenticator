package com.moraes.authenticator.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.moraes.authenticator.api.util.MessagesUtil;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends PatternException {

    public ResourceNotFoundException() {
        super(MessagesUtil.getMessage(MessagesUtil.RESOURCE_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}

package com.moraes.authenticator.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.moraes.authenticator.api.util.MessagesUtil;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedBasic extends PatternException {

    public UnauthorizedBasic() {
        super(MessagesUtil.getMessage(MessagesUtil.USER_NOT_PERMISSION), HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedBasic(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}

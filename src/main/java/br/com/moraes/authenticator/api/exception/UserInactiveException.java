package br.com.moraes.authenticator.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.moraes.authenticator.api.util.StringUtil;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserInactiveException extends PatternException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public UserInactiveException() {
        this(StringUtil.getMessage("user_inactive"));
    }

    public UserInactiveException(String message) {
        this(message, HttpStatus.FORBIDDEN);
    }

    public UserInactiveException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
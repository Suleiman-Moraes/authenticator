package br.com.moraes.authenticator.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidJwtAuthenticationException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Getter
    protected HttpStatus httpStatus = HttpStatus.FORBIDDEN;

	public InvalidJwtAuthenticationException(String msg) {
		super(msg);
	}
}

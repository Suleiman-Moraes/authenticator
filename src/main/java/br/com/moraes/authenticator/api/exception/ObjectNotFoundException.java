package br.com.moraes.authenticator.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.moraes.authenticator.api.util.StringUtil;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ObjectNotFoundException extends PatternException{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public ObjectNotFoundException(){
        this(StringUtil.getMessage("object_not_found"));
    }

    public ObjectNotFoundException(String message) {
        this(message, HttpStatus.NOT_FOUND);
    }

    public ObjectNotFoundException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
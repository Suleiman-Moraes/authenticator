package br.com.moraes.authenticator.api.exception.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiError {
	
    private List<String> messages;
    private String messageError;
    private String messageDevelop;
    private Integer status;
    private String error;
    private Date timestamp;

    public ApiError(String message, String messageError, String messageDevelop, HttpStatus httpStatus) {
        timestamp = new Date();
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.messages = Arrays.asList(message);
        this.messageDevelop = messageDevelop;
        this.messageError = messageError;
    }
    
    @Builder(builderMethodName = "builder")
    public ApiError(List<String> messages, String messageError, String messageDevelop, HttpStatus httpStatus, Date timestamp) {
        this.messageError = messageError;
        this.messages = messages;
        this.messageDevelop = messageDevelop;
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.timestamp = timestamp == null ? new Date() : timestamp;
    }
}
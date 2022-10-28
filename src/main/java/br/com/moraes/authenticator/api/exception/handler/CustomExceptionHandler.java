package br.com.moraes.authenticator.api.exception.handler;

import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.moraes.authenticator.api.exception.IdNotInformedException;
import br.com.moraes.authenticator.api.exception.InvalidJwtAuthenticationException;
import br.com.moraes.authenticator.api.exception.ObjectNotFoundException;
import br.com.moraes.authenticator.api.exception.PatternException;
import br.com.moraes.authenticator.api.exception.UserInactiveException;
import br.com.moraes.authenticator.api.exception.ValidException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(value = { ValidException.class })
	protected ResponseEntity<Object> handleValidException(ValidException ex, WebRequest request) {
		final String mensagemExcecao = mensagemExcecao(request.getParameterMap(), ex.getErrs().toString());
		log.error("###Erro no ApiExceptionHandler com a mensagem: |{}|, e a mensagem develop: |{}|", mensagemExcecao,
				ExceptionUtils.getRootCauseMessage(ex));
		return handleExceptionInternal(ex,
				ApiError.builder().messages(ex.getErrs()).messageError(mensagemExcecao)
						.messageDevelop(ExceptionUtils.getRootCauseMessage(ex)).httpStatus(ex.getHttpStatus()).build(),
				new HttpHeaders(), ex.getHttpStatus(), request);
	}

	/**
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(value = { PatternException.class, IdNotInformedException.class, ObjectNotFoundException.class,
			UserInactiveException.class })
	protected ResponseEntity<Object> handlePatternException(PatternException ex, WebRequest request) {
		return tratar(ex, request, ex.getMessage(), ex.getHttpStatus());
	}

	/**
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(value = { InvalidJwtAuthenticationException.class })
	protected ResponseEntity<Object> handleInvalidJwtAuthenticationException(InvalidJwtAuthenticationException ex, WebRequest request) {
		return tratar(ex, request, ex.getMessage(), ex.getHttpStatus());
	}
	/**
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(value = { IllegalArgumentException.class })
	protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
		return tratar(ex, request, ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	/**
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(value = { Exception.class, NullPointerException.class })
	protected ResponseEntity<Object> handleExceptionGeneric(Exception ex, WebRequest request) {
		return tratar(ex, request, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 
	 * @param ex
	 * @param request
	 * @param message
	 * @param httpStatus
	 * @return
	 */
	private ResponseEntity<Object> tratar(Exception ex, WebRequest request, String message, HttpStatus httpStatus) {
		final String mensagemExcecao = mensagemExcecao(request.getParameterMap(), message);
		switch (httpStatus) {
		case INTERNAL_SERVER_ERROR:
		case BAD_REQUEST:
			log.error("###Erro no ApiExceptionHandler com a mensagem: |{}|, e a mensagem develop: |{}|",
					mensagemExcecao, ExceptionUtils.getRootCauseMessage(ex));
			break;
		default:
			break;
		}
		return handleExceptionInternal(ex,
				new ApiError(mensagemExcecao, ex.getMessage(), ExceptionUtils.getRootCauseMessage(ex), httpStatus),
				new HttpHeaders(), httpStatus, request);
	}

	/**
	 * Formata a mensagem com o parâmetro passado na requisição mais a mensagem da
	 * exceção.
	 * 
	 * @param uriParams Parâmetro da requisição HTTP.
	 * @param mensagem  Mensagem da Exception.
	 * @return Mensagem com o parâmetro passado na requisição mais a mensagem da
	 *         exceção.
	 */
	private String mensagemExcecao(Map<String, String[]> uriParams, String mensagem) {
		StringBuilder tudo = new StringBuilder("PARAMETROS { ");
		uriParams.forEach((k, v) -> {
			tudo.append(String.format("%s=", k));
			for (String x : v) {
				tudo.append(x).append("");
			}
			tudo.append(", ");
		});
		tudo.append("} ");
		tudo.append(mensagem);
		return tudo.toString().replaceAll(", }", " }");
	}
}
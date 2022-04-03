package br.com.challenge.entities.handler.error;


import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.challenge.dto.ResponseErrorDto;
import br.com.challenge.entities.handler.LogHandler;

@RestControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler{
	
	private HttpHeaders headers = new HttpHeaders();
	
	@ExceptionHandler(InternalSeverError.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ResponseErrorDto> customInternalSeverError(Exception ex, WebRequest request){
		return new ResponseEntity<>(getResponseErrorDto(ex.getMessage(), request), headers, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(BadRequestError.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ResponseErrorDto> customBadRequestError(Exception ex, WebRequest request){
		return new ResponseEntity<>(getResponseErrorDto(ex.getMessage(), request), headers, HttpStatus.BAD_REQUEST);
	}
	
	private ResponseErrorDto getResponseErrorDto(String error, WebRequest request) {
		LogHandler requestData = new LogHandler(null, request.getContextPath(), request.getParameter("body"), LocalDateTime.now());
		return new ResponseErrorDto(error, requestData.toString());
	}
}

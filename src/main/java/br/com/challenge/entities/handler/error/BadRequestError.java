package br.com.challenge.entities.handler.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseBody
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class BadRequestError extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public BadRequestError(String message) {
		super(message);
	}
}

package br.com.challenge.dto;

public class ResponseErrorDto {
	private String error;
	private String request;
	
	public ResponseErrorDto(String error, String request) {
		this.error = error;
		this.request = request;
	}

	public String getError() {
		return error;
	}

	public String getRequest() {
		return request;
	}
}

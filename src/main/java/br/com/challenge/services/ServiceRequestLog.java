package br.com.challenge.services;

public interface ServiceRequestLog {

	void logRequest(String method, String endPoint, String data);
}

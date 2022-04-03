package br.com.challenge.services;

import org.springframework.http.ResponseEntity;

public interface ServiceRole<T> {
	ResponseEntity<T>putRole(T entityRole);
}

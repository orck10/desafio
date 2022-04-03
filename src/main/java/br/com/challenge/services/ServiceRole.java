package br.com.challenge.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface ServiceRole<T> {
	
	ResponseEntity<T> putRole(T entityRole);
	
	ResponseEntity<List<String>> newRoleEntity(T entity);
}

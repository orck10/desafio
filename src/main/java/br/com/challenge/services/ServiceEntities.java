package br.com.challenge.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface ServiceEntities<T> {
	public ResponseEntity<List<T>> insertList(List<T> entities);
	
	public ResponseEntity<T> insertInsertOne(T entities);
	
	public ResponseEntity<T> getEntity(String id);
	
	public ResponseEntity<List<T>> getAll();
}

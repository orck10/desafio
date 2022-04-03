package br.com.challenge.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import br.com.challenge.dto.DataPresenter;

public interface ServiceEntities<T> {
	public ResponseEntity<List<T>> insertList(List<T> entities);
	
	public ResponseEntity<T> insertInsertOne(T entities);
	
	public ResponseEntity<T> getEntity(String id);
	
	public ResponseEntity<DataPresenter> getAll(Integer page, Integer size);
	
	public ResponseEntity<T> setTeamLead(String userId, String teamId);
}

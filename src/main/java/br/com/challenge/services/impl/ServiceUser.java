package br.com.challenge.services.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.challenge.entities.User;
import br.com.challenge.services.ServiceEntities;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ServiceUser implements ServiceEntities<User>{

	@Override
	public ResponseEntity<List<User>> insertList(List<User> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<User> insertInsertOne(User entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<User> getEntity(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<List<User>> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}

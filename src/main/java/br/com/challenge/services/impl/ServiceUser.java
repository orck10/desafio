package br.com.challenge.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.challenge.dto.DataPresenter;
import br.com.challenge.dto.UserPresenter;
import br.com.challenge.dto.parser.UserPresenterParser;
import br.com.challenge.entities.challenge.User;
import br.com.challenge.entities.handler.error.BadRequestError;
import br.com.challenge.entities.handler.error.InternalSeverError;
import br.com.challenge.repositories.challenge.RoleRepo;
import br.com.challenge.repositories.challenge.UserRepo;
import br.com.challenge.services.ServiceEntities;

@Service
public class ServiceUser implements ServiceEntities<UserPresenter>{
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private RoleRepo<UserPresenter, User> roleRepo;

	@Override
	public ResponseEntity<List<UserPresenter>> insertList(List<UserPresenter> entities) {
		try {
			List<UserPresenter> resp = new ArrayList<>();
			entities.forEach(e -> resp.add(saveUser(e)));
			return new ResponseEntity<List<UserPresenter>>(resp, HttpStatus.OK);
		}catch (Exception e) {
			throw new InternalSeverError(e.getMessage());
		}
	}

	@Override
	public ResponseEntity<UserPresenter> insertInsertOne(UserPresenter entities) {
		try {
			return new ResponseEntity<>(saveUser(entities), HttpStatus.OK);
		}catch (Exception e) {
			throw new InternalSeverError(e.getMessage());
		}
	}

	@Override
	public ResponseEntity<UserPresenter> getEntity(String id) {
		try {
			Optional<User> inDb = userRepo.findById(id);
			if(inDb.isEmpty()) throw new BadRequestError("Entidade não encontrada");
			return new ResponseEntity<>(roleRepo.findEntity(inDb.get()).get() ,HttpStatus.OK);
		}catch (BadRequestError e) {
			throw e;
		}
		catch (Exception e) {
			throw new InternalSeverError(e.getMessage());
		}
	}
	
	private UserPresenter saveUser(UserPresenter presenter) {
		Optional<User> inDb = userRepo.findById(presenter.getId());
		return inDb.isEmpty() || !inDb.isPresent() ? insertUser(presenter) : 
			roleRepo.findEntity(inDb.get()).get();
	}
	
	private UserPresenter insertUser(UserPresenter presenter) {
		userRepo.save(UserPresenterParser.presenterToUser(presenter));
		return roleRepo.insert(presenter).get();
	}
	
	@Override
	public ResponseEntity<DataPresenter> getAll(Integer page, Integer size) {
		try {
			return new ResponseEntity<>(roleRepo.findAllWithPag(page, size), HttpStatus.OK);
		}catch (Exception e) {
			throw new InternalSeverError(e.getMessage());
		}
	}
	/*
	 * 
	 * 
	 * 
	 * TO DO
	 */
	@Override
	public ResponseEntity<UserPresenter> setTeamLead(String userId, String teamId) {
		// TODO Auto-generated method stub
		return null;
	}
}

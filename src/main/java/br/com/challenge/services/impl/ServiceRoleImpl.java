package br.com.challenge.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.challenge.dto.RolePresenter;
import br.com.challenge.dto.parser.RolePresenterParser;
import br.com.challenge.entities.challenge.Role;
import br.com.challenge.entities.challenge.RoleEntity;
import br.com.challenge.entities.handler.error.BadRequestError;
import br.com.challenge.entities.handler.error.InternalSeverError;
import br.com.challenge.repositories.challenge.RoleEntityRepo;
import br.com.challenge.repositories.challenge.RoleManagerRepo;
import br.com.challenge.services.ServiceRole;

@Service
public class ServiceRoleImpl implements ServiceRole<RolePresenter>{
	
	@Autowired
	private RoleManagerRepo roleRepo;
	
	@Autowired
	private RoleEntityRepo roleEntityRepo;
	
	private Logger log = LogManager.getLogger(ServiceRoleImpl.class);
	

	@Override
	public ResponseEntity<RolePresenter> putRole(RolePresenter entityRole) {
		try {
			Role role = RolePresenterParser.presenterToRole(entityRole);
			role = roleRepo.save(role);
			return new ResponseEntity<>(RolePresenterParser.RoleToPresenter(role), HttpStatus.OK);
		}catch (Exception e) {
			log.error(e.getMessage());
			throw new InternalSeverError(e.getMessage());
		}
	}

	/*
	 * Function to show stream use.
	 * 
	 */
	@Override
	public ResponseEntity<List<String>> newRoleEntity(RolePresenter entity) {
		try {
			if(entity.getRole() == null || entity.getRole().isEmpty()) throw new BadRequestError("O campo role não pode estar vazio");
			RoleEntity role = new RoleEntity(entity.getRole());
			roleEntityRepo.save(role);
			List<RoleEntity> all = roleEntityRepo.findAll();
			List<String> roleList = new ArrayList<>();
			all.stream().forEach(r -> roleList.add(r.getRoleName()));
			return new ResponseEntity<>(roleList, HttpStatus.OK);
		}catch (BadRequestError e) {
			log.error(e.getMessage());
			throw e;
		}
		catch (Exception e) {
			log.error(e.getMessage());
			throw new InternalSeverError(e.getMessage());
		}
	}
	
	
}

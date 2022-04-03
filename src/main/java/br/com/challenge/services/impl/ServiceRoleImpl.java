package br.com.challenge.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.challenge.dto.RolePresenter;
import br.com.challenge.dto.parser.RolePresenterParser;
import br.com.challenge.entities.handler.error.InternalSeverError;
import br.com.challenge.repositories.challenge.RoleManagerRepo;
import br.com.challenge.services.ServiceRole;

@Service
public class ServiceRoleImpl implements ServiceRole<RolePresenter>{
	
	@Autowired
	private RoleManagerRepo roleRepo;
	
	private Logger log = LogManager.getLogger(ServiceRoleImpl.class);
	

	@Override
	public ResponseEntity<RolePresenter> putRole(RolePresenter entityRole) {
		try {
			return new ResponseEntity<>(RolePresenterParser.RoleToPresenter(roleRepo.save(RolePresenterParser.presenterToRole(entityRole))), HttpStatus.OK);
		}catch (Exception e) {
			log.error(e.getMessage());
			throw new InternalSeverError(e.getMessage());
		}
	}
	
	
}

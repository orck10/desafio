package br.com.challenge.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.challenge.dto.DataPresenter;
import br.com.challenge.dto.TeamPresenter;
import br.com.challenge.dto.parser.TeamPresenterParser;
import br.com.challenge.entities.challenge.Team;
import br.com.challenge.entities.challenge.User;
import br.com.challenge.entities.handler.error.BadRequestError;
import br.com.challenge.entities.handler.error.InternalSeverError;
import br.com.challenge.repositories.challenge.RoleRepo;
import br.com.challenge.repositories.challenge.TeamRepo;
import br.com.challenge.repositories.challenge.UserRepo;
import br.com.challenge.services.ServiceEntities;

@Service
public class ServiceTeam implements ServiceEntities<TeamPresenter>{
	
	@Autowired
	private TeamRepo teamRepo;
	
	@Autowired
	private RoleRepo<TeamPresenter, Team> roleRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	private Logger log = LogManager.getLogger(ServiceTeam.class);
	
	@Override
	public ResponseEntity<List<TeamPresenter>> insertList(List<TeamPresenter> entities) {
		try {
			List<TeamPresenter> resp = new ArrayList<>();
			entities.forEach(e -> resp.add(saveTeam(e)));
			return new ResponseEntity<>(resp, HttpStatus.OK);
		}catch (Exception e) {
			log.error(e.getMessage());
			throw new InternalSeverError(e.getMessage());
		}
	}

	@Override
	public ResponseEntity<TeamPresenter> insertInsertOne(TeamPresenter entities) {
		try {
			return new ResponseEntity<>(saveTeam(entities), HttpStatus.OK);
		}catch (Exception e) {
			log.error(e.getMessage());
			throw new InternalSeverError(e.getMessage());
		}
	}

	@Override
	public ResponseEntity<TeamPresenter> getEntity(String id) {
		try {
			Optional<Team> inDb = teamRepo.findById(id);
			if(inDb.isEmpty()) throw new BadRequestError("Entidade não encontrada");
			return new ResponseEntity<>(roleRepo.findEntity(inDb.get()).get() ,HttpStatus.OK);
		}catch (BadRequestError e) {
			log.error(e.getMessage());
			throw e;
		}
		catch (Exception e) {
			log.error(e.getMessage());
			throw new InternalSeverError(e.getMessage());
		}
	}

	@Override
	public ResponseEntity<DataPresenter> getAll(Integer page, Integer size) {
		try {
			return new ResponseEntity<>(roleRepo.findAllWithPag(page, size), HttpStatus.OK);
		}catch (Exception e) {
			log.error(e.getMessage());
			throw new InternalSeverError(e.getMessage());
		}
	}

	private TeamPresenter saveTeam(TeamPresenter presenter) {
		Optional<Team> inDb = teamRepo.findById(presenter.getId());
		return !inDb.isPresent() ? insertTeam(presenter) : 
					roleRepo.findEntity(inDb.get()).get();
	}
	
	private TeamPresenter insertTeam(TeamPresenter presenter) {
		presenter.setTeamLead(verifyTeamLeader(presenter));
		teamRepo.save(TeamPresenterParser.presenterToTeam(presenter));
		TeamPresenter saved = roleRepo.insert(presenter).get();
		return saved;
	}
	
	private User verifyTeamLeader(TeamPresenter presenter) {
		if(presenter.getTeamLead() != null) return presenter.getTeamLead();
		if(presenter.getTeamLeadId() == null) return null;
		Optional<User> teamLead = userRepo.findById(presenter.getTeamLeadId());
		return teamLead.isEmpty() ? null : teamLead.get();
	}

	@Override
	public ResponseEntity<TeamPresenter> setTeamLead(String userId, String teamId) {
		try {
			Optional<User> lead = userRepo.findById(userId);
			if(lead.isEmpty()) throw new BadRequestError("Lider não consta na base");
			Optional<Team> team = teamRepo.findById(teamId);
			if(team.isEmpty()) throw new BadRequestError("Time não consta na base");
			team.get().setLead(lead.get());
			return  new ResponseEntity<>(TeamPresenterParser.teamToPresenter(teamRepo.save(team.get())), HttpStatus.OK);
			
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

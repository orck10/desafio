package br.com.challenge.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.challenge.dto.DataPresenter;
import br.com.challenge.dto.MetaData;
import br.com.challenge.dto.TeamPresenter;
import br.com.challenge.dto.parser.TeamPresenterParser;
import br.com.challenge.entities.challenge.Team;
import br.com.challenge.entities.challenge.User;
import br.com.challenge.entities.handler.error.BadRequestError;
import br.com.challenge.repositories.challenge.RoleRepo;
import br.com.challenge.repositories.challenge.TeamRepo;
import br.com.challenge.repositories.challenge.UserRepo;

@RunWith(MockitoJUnitRunner.class)
public class TestServiceTeam {
	
	@Mock
	private TeamRepo teamRepo;
	
	@Mock
	private RoleRepo<TeamPresenter, Team> roleRepo;
	
	@Mock
	private UserRepo userRepo;
	
	@InjectMocks
	private ServiceTeam serviceTeam;
	
	private List<TeamPresenter> getTeamPresenterList(){
		List<TeamPresenter> t = new ArrayList<>();
		for(int i = 0; i < 5; i++) {	
			t.add(getTeamPresenter());
		}
		return t;
	}
	
	private TeamPresenter getTeamPresenter() {
		TeamPresenter team = new TeamPresenter(UUID.randomUUID().toString(), UUID.randomUUID().toString(), null);
		team.setTeamLeadId(UUID.randomUUID().toString());
		return team;
	}
	
	private User getUser() {
		User user = new User(UUID.randomUUID().toString(), UUID.randomUUID().toString());
		return user;
	}
	
	private Team getTeam() {
		Team team = new Team(UUID.randomUUID().toString(), UUID.randomUUID().toString(), null);
		return team;
	}
	
	@Test
	public void insertList_Sucess_SimpleEntity() {
		List<TeamPresenter> in = getTeamPresenterList();
		when(teamRepo.findById(Mockito.anyString())).thenReturn(Optional.empty());
		when(roleRepo.insert(Mockito.any())).thenReturn(Optional.of(in.get(0)));
		
		ResponseEntity<List<TeamPresenter>> out = serviceTeam.insertList(in);
		
		verify(roleRepo, atLeastOnce()).insert(in.get(0));
		assertEquals(in.get(0), out.getBody().get(0));
		assertEquals(HttpStatus.OK, out.getStatusCode());
		assertEquals(in.get(0).getTeamLeadId(), out.getBody().get(0).getTeamLeadId());
		assertEquals(in.get(0).hashCode(), out.getBody().get(0).hashCode());
	}
	
	@Test
	public void insertList_Error_On_Insert() {
		List<TeamPresenter> in = getTeamPresenterList();
		String message = "Run time error";
		
		when(teamRepo.findById(Mockito.anyString())).thenReturn(Optional.empty());
		when(roleRepo.insert(Mockito.any())).thenThrow(new RuntimeException(message));
		try {
			serviceTeam.insertList(in);
			fail();
		}catch (Exception e) {
			verify(teamRepo, times(1)).findById(in.get(0).getId());
			assertEquals(message, e.getMessage());
		}
	}
	
	@Test
	public void insertInsertOne_Sucess() {
		TeamPresenter in = getTeamPresenter();
		when(teamRepo.findById(Mockito.anyString())).thenReturn(Optional.empty());
		when(roleRepo.insert(in)).thenReturn(Optional.of(in));
		
		ResponseEntity<TeamPresenter> out = serviceTeam.insertInsertOne(in);
		
		assertEquals(in, out.getBody());
		assertEquals(HttpStatus.OK, out.getStatusCode());
	}
	
	@Test
	public void insertInsertOne_Sucess_With_Team_Leader() {
		TeamPresenter in = getTeamPresenter();
		in.setTeamLead(getUser());
		when(teamRepo.findById(Mockito.anyString())).thenReturn(Optional.empty());
		when(roleRepo.insert(in)).thenReturn(Optional.of(in));
		
		ResponseEntity<TeamPresenter> out = serviceTeam.insertInsertOne(in);
		
		assertEquals(in, out.getBody());
		assertEquals(HttpStatus.OK, out.getStatusCode());
	}
	
	@Test
	public void insertInsertOne_Sucess_With_No_Team_Leader() {
		TeamPresenter in = getTeamPresenter();
		in.setTeamLeadId(null);
		when(teamRepo.findById(Mockito.anyString())).thenReturn(Optional.empty());
		when(roleRepo.insert(in)).thenReturn(Optional.of(in));
		
		ResponseEntity<TeamPresenter> out = serviceTeam.insertInsertOne(in);
		
		assertEquals(in, out.getBody());
		assertEquals(HttpStatus.OK, out.getStatusCode());
	}
	
	@Test
	public void insertInsertOne_Sucess_With_Team_LeaderId_Not_On_DB() {
		TeamPresenter in = getTeamPresenter();
		when(teamRepo.findById(Mockito.anyString())).thenReturn(Optional.empty());
		when(roleRepo.insert(in)).thenReturn(Optional.of(in));
		when(userRepo.findById(in.getTeamLeadId())).thenReturn(Optional.empty());
		
		ResponseEntity<TeamPresenter> out = serviceTeam.insertInsertOne(in);
		
		assertEquals(in, out.getBody());
		assertNull(out.getBody().getTeamLead());
		assertEquals(HttpStatus.OK, out.getStatusCode());
	}
	
	@Test
	public void insertInsertOne_Sucess_With_Team_LeaderId_On_DB() {
		TeamPresenter in = getTeamPresenter();
		User teamLeader  = getUser();
		in.setTeamLeadId(teamLeader.getId());
		when(teamRepo.findById(Mockito.anyString())).thenReturn(Optional.empty());
		when(roleRepo.insert(in)).thenReturn(Optional.of(in));
		when(userRepo.findById(in.getTeamLeadId())).thenReturn(Optional.of(teamLeader));
		
		ResponseEntity<TeamPresenter> out = serviceTeam.insertInsertOne(in);
		
		assertEquals(in, out.getBody());
		assertEquals(teamLeader, out.getBody().getTeamLead());
		assertEquals(HttpStatus.OK, out.getStatusCode());
	}
	
	@Test
	public void insertInsertOne_Erro() {
		TeamPresenter in = getTeamPresenter();
		String message = "run time error";
		when(teamRepo.findById(Mockito.anyString())).thenThrow(new RuntimeException(message));
		try {
			serviceTeam.insertInsertOne(in);
			fail();
		}catch (Exception e) {
			assertEquals(message, e.getMessage());
		}
	}
	
	@Test
	public void insertInsertOne_Sucess_Without_Save() {
		TeamPresenter in = getTeamPresenter();
		when(teamRepo.findById(in.getId())).thenReturn(Optional.of(TeamPresenterParser.presenterToTeam(in)));
		when(roleRepo.findEntity(Mockito.any())).thenReturn(Optional.of(in));
		
		ResponseEntity<TeamPresenter> out = serviceTeam.insertInsertOne(in);
		
		assertEquals(in, out.getBody());
		assertEquals(HttpStatus.OK, out.getStatusCode());
	}
	
	@Test
	public void getEntity_Sucess() {
		TeamPresenter team = getTeamPresenter();
		when(teamRepo.findById(team.getId())).thenReturn(Optional.of(TeamPresenterParser.presenterToTeam(team)));
		when(roleRepo.findEntity(Mockito.any())).thenReturn(Optional.of(team));
		
		ResponseEntity<TeamPresenter> out = serviceTeam.getEntity(team.getId());
		
		verify(teamRepo, times(1)).findById(team.getId());
		verify(roleRepo, times(1)).findEntity(Mockito.any());
		assertEquals(HttpStatus.OK, out.getStatusCode());
		assertEquals(team.toString(), out.getBody().toString());
	}
	
	@Test
	public void getEntity_Error_Not_In_DB() {
		String id = UUID.randomUUID().toString();
		when(teamRepo.findById(id)).thenReturn(Optional.empty());
		try {
			serviceTeam.getEntity(id);
			fail();
		}catch (Exception e) {
			assertEquals(BadRequestError.class, e.getClass());
			assertEquals("Entidade não encontrada", e.getMessage());
		}
	}
	
	@Test
	public void getEntity_Error_Other() {
		String id = UUID.randomUUID().toString();
		String message = "Error_Other";
		when(teamRepo.findById(id)).thenThrow(new RuntimeException(message));
		try {
			serviceTeam.getEntity(id);
			fail();
		}catch (Exception e) {
			assertEquals(message, e.getMessage());
		}
	}
	
	@Test
	public void getAll_Sucess() {
		MetaData metaData = new MetaData(1, 10, 2);
		DataPresenter data = new DataPresenter("String", metaData);
		when(roleRepo.findAllWithPag(1, 10)).thenReturn(data);
		
		ResponseEntity<DataPresenter> out = serviceTeam.getAll(1, 10);
		
		verify(roleRepo, times(1)).findAllWithPag(1, 10);
		assertEquals(data, out.getBody());
		assertEquals(1, out.getBody().getMeta().getPage().intValue());
		assertEquals(10, out.getBody().getMeta().getSize().intValue());
		assertEquals(2, out.getBody().getMeta().getLastPage().intValue());
		assertEquals(HttpStatus.OK, out.getStatusCode());
	}
	
	@Test
	public void getAll_Error() {
		String message = "getAll_Error"; 
		when(roleRepo.findAllWithPag(1, 10)).thenThrow(new RuntimeException(message));
		try {
			serviceTeam.getAll(1, 10);
			fail();
		}catch (Exception e) {
			assertEquals(message, e.getMessage());
		}
	}
	
	@Test
	public void setTeamLead_NO_Lead_On_DB() {
		String userId = UUID.randomUUID().toString(); 
		String teamId = UUID.randomUUID().toString();
		
		when(userRepo.findById(userId)).thenReturn(Optional.empty());
		
		try {
			serviceTeam.setTeamLead(userId, teamId);
		}catch (Exception e) {
			assertEquals(BadRequestError.class, e.getClass());
			assertEquals("Lider não consta na base", e.getMessage());
		}
	}
	
	@Test
	public void setTeamLead_NO_Team_On_DB() {
		User user = getUser(); 
		String teamId = UUID.randomUUID().toString();
		
		when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
		when(teamRepo.findById(teamId)).thenReturn(Optional.empty());
		
		try {
			serviceTeam.setTeamLead(user.getId(), teamId);
		}catch (Exception e) {
			assertEquals(BadRequestError.class, e.getClass());
			assertEquals("Time não consta na base", e.getMessage());
		}
	}
	
	@Test
	public void setTeamLead_Sucess() {
		User user = getUser(); 
		Team team = getTeam();
		team.setLead(user);
		
		when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
		when(teamRepo.findById(team.getId())).thenReturn(Optional.of(team));
		when(teamRepo.save(team)).thenReturn(team);
		
		ResponseEntity<TeamPresenter> out = serviceTeam.setTeamLead(user.getId(), team.getId());
		
		assertEquals(user, out.getBody().getTeamLead());
		assertEquals(team.toString(), TeamPresenterParser.presenterToTeam(out.getBody()).toString());
	}
	
	@Test
	public void setTeamLead_OtherError() {
		String message = "setTeamLead_OtherError";
		
		when(userRepo.findById(Mockito.anyString())).thenThrow(new RuntimeException("setTeamLead_OtherError"));
		try {
			serviceTeam.setTeamLead("", "");
			fail();
		}catch (Exception e) {
			assertEquals(message, e.getMessage());
		}
	}
}

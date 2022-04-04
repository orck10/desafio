package br.com.challenge.services.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
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
import org.springframework.http.ResponseEntity;

import com.google.gson.Gson;

import br.com.challenge.dto.TeamPresenter;
import br.com.challenge.entities.challenge.Team;
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
		for(int i = 0; i < 10; i++) {
			t.add(new TeamPresenter(UUID.randomUUID().toString(), UUID.randomUUID().toString(), null));
		}
		return t;
	}
	
	@Test
	public void insertList_Sucess_SimpleEntity() {
		List<TeamPresenter> in = getTeamPresenterList();
		when(teamRepo.findById(Mockito.anyString())).thenReturn(Optional.empty());
		when(roleRepo.insert(Mockito.any())).thenReturn(Optional.of(in.get(0)));
		
		ResponseEntity<List<TeamPresenter>> out = serviceTeam.insertList(in);
		
		verify(roleRepo, atLeastOnce()).insert(in.get(0));
		assertEquals(in.get(0), out.getBody().get(0));
	}
}

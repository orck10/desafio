package br.com.challenge.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.challenge.dto.RolePresenter;
import br.com.challenge.entities.challenge.Role;
import br.com.challenge.entities.challenge.RoleEntity;
import br.com.challenge.entities.challenge.Team;
import br.com.challenge.entities.challenge.User;
import br.com.challenge.entities.handler.error.BadRequestError;
import br.com.challenge.repositories.challenge.RoleEntityRepo;
import br.com.challenge.repositories.challenge.RoleManagerRepo;
import br.com.challenge.repositories.challenge.TeamRepo;
import br.com.challenge.repositories.challenge.UserRepo;

@RunWith(MockitoJUnitRunner.class)
public class TestServiceRoleImpl {
	
	@Mock
	private RoleManagerRepo roleRepo;
	
	@Mock
	private RoleEntityRepo roleEntityRepo;
	
	@Mock
	private UserRepo userRepo;
	
	@Mock
	private TeamRepo teamRepo;
	
	@InjectMocks
	private ServiceRoleImpl serviceRoleImpl;
	
	private RolePresenter getRolePresenter() {
		return new RolePresenter("Teste", UUID.randomUUID().toString(), UUID.randomUUID().toString());
	}
	
	@Test
	public void putRole_Sucess() {
		RolePresenter rolePresenter = getRolePresenter();
		Role role = new Role(rolePresenter.getUserId(), rolePresenter.getTeamID(), rolePresenter.getRole());
		when(userRepo.findById(Mockito.any())).thenReturn(Optional.of(new User()));
		when(teamRepo.findById(Mockito.any())).thenReturn(Optional.of(new Team()));
		when(roleRepo.save(Mockito.any())).thenReturn(role);
		
		ResponseEntity<RolePresenter> out = serviceRoleImpl.putRole(rolePresenter);
		
		verify(roleRepo, atLeastOnce()).save(Mockito.any());
		assertEquals(rolePresenter.toString(), out.getBody().toString());
		assertEquals(HttpStatus.OK, out.getStatusCode());
	}
	
	@Test
	public void putRole_Save_Error() {
		RolePresenter rolePresenter = getRolePresenter();
		String errorMessage = "Teste Erro";
		
		when(userRepo.findById(Mockito.any())).thenReturn(Optional.of(new User()));
		when(teamRepo.findById(Mockito.any())).thenReturn(Optional.of(new Team()));
		when(roleRepo.save(Mockito.any())).thenThrow(new BadRequestError(errorMessage));
		try {
			ResponseEntity<RolePresenter> out = serviceRoleImpl.putRole(rolePresenter);
			fail();
		}catch (Exception e) {
			assertEquals(errorMessage, e.getMessage());
		}
		verify(roleRepo, atLeastOnce()).save(Mockito.any());
	}
	
	@Test
	public void newRoleEntity_Sucess() {
		RolePresenter in = getRolePresenter();
		RoleEntity role = new RoleEntity(in.getRole());
		List<RoleEntity> roles = new ArrayList<RoleEntity>();
		roles.add(role);
		when(roleEntityRepo.findAll()).thenReturn(roles);
		
		ResponseEntity<List<String>> out = serviceRoleImpl.newRoleEntity(in);
		
		verify(roleEntityRepo, atLeastOnce()).save(Mockito.any());
		
		assertEquals(roles.size(), out.getBody().size());
		assertEquals(role.getRoleName(), out.getBody().get(0));
		
	}
	
	@Test
	public void newRoleEntity_NULL_Role() {
		RolePresenter in = getRolePresenter();
		in.setRole(null);
		RoleEntity role = new RoleEntity(null);


		try {
			ResponseEntity<List<String>> out = serviceRoleImpl.newRoleEntity(in);
			fail();
		}catch (BadRequestError e) {
			assertEquals("O campo role não pode estar vazio", e.getMessage());
		}
	}
	
	@Test
	public void newRoleEntity_Other_error() {
		RolePresenter in = getRolePresenter();
		RoleEntity role = new RoleEntity(null);
		String errorMessage = "Error";
		when(roleEntityRepo.save(Mockito.any())).thenThrow(new RuntimeException(errorMessage));

		try {
			ResponseEntity<List<String>> out = serviceRoleImpl.newRoleEntity(in);
			fail();
		}catch (Exception e) {
			assertEquals(errorMessage, e.getMessage());
		}
	}
}

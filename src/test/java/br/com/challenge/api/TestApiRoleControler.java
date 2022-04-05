package br.com.challenge.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.challenge.dto.ResponseErrorDto;
import br.com.challenge.dto.RolePresenter;
import br.com.challenge.entities.challenge.RoleEntity;
import br.com.challenge.enums.DefaltRoleEnum;
import br.com.challenge.repositories.challenge.RoleEntityRepo;
import br.com.challenge.repositories.handler.LogHandlerRepo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class TestApiRoleControler {
	
	@Autowired
    private TestRestTemplate testRestTemplate;
	
	@Autowired
	private LogHandlerRepo logHandlerRepo;
	
	@Autowired
	private RoleEntityRepo roleEntityRepo;
	
	@Test
	public void postRole_POST_SUCCESS() {
		RolePresenter role = new RolePresenter(null, 
				"94e43ccd-a638-4df8-8885-a7a1bb384c03", 
				"7a967a98-e7ef-4009-9e87-b500e26f9b45");
		
		Long nBefore = logHandlerRepo.count();
		HttpEntity<RolePresenter> httpEntity = new HttpEntity<>(role);
		ResponseEntity<RolePresenter> out = testRestTemplate.exchange("/v1/role/", HttpMethod.POST, httpEntity, RolePresenter.class);
		
		Long nAfter = logHandlerRepo.count();
		
		assertEquals(nBefore.longValue()+1, nAfter.longValue());
		assertEquals(HttpStatus.OK, out.getStatusCode());
		assertEquals(role.getTeamID(), out.getBody().getTeamID());
		assertEquals(role.getUserId(), out.getBody().getUserId());
		assertEquals(DefaltRoleEnum.DEVELOPER.getRole(), out.getBody().getRole());
	}
	
	@Test
	public void postRole_POST_BAD_REQUEST_NO_USER_ID() {
		RolePresenter role = new RolePresenter(null, 
				null, 
				"7a967a98-e7ef-4009-9e87-b500e26f9b45");
		
		Long nBefore = logHandlerRepo.count();
		HttpEntity<RolePresenter> httpEntity = new HttpEntity<>(role);
		ResponseEntity<ResponseErrorDto> out = testRestTemplate.exchange("/v1/role/", HttpMethod.POST, httpEntity, ResponseErrorDto.class);
		
		Long nAfter = logHandlerRepo.count();
		
		assertEquals(nBefore.longValue()+1, nAfter.longValue());
		assertEquals(HttpStatus.BAD_REQUEST, out.getStatusCode());
		assertEquals("O campo UserId não pode estar vazio", out.getBody().getError());
	}
	
	@Test
	public void postRole_POST_BAD_REQUEST_NO_TEAM_ID() {
		RolePresenter role = new RolePresenter(null, 
				"7a967a98-e7ef-4009-9e87-b500e26f9b45", 
				null);
		
		Long nBefore = logHandlerRepo.count();
		HttpEntity<RolePresenter> httpEntity = new HttpEntity<>(role);
		ResponseEntity<ResponseErrorDto> out = testRestTemplate.exchange("/v1/role/", HttpMethod.POST, httpEntity, ResponseErrorDto.class);
		
		Long nAfter = logHandlerRepo.count();
		
		assertEquals(nBefore.longValue()+1, nAfter.longValue());
		assertEquals(HttpStatus.BAD_REQUEST, out.getStatusCode());
		assertEquals("O campo TeamId não pode estar vazio", out.getBody().getError());
	}
	
	@Test
	public void newRole_POST_SUCCESS() {
		Long nBefore = logHandlerRepo.count();
		
		String newRole = UUID.randomUUID().toString();
		RolePresenter role = new RolePresenter(newRole, null, null);
		HttpEntity<RolePresenter> httpEntity = new HttpEntity<>(role);
		
		ResponseEntity<String> out = testRestTemplate.exchange("/v1/role/new", HttpMethod.POST, httpEntity, String.class);
		
		List<RoleEntity> all = roleEntityRepo.findAll();
		
		Long nAfter = logHandlerRepo.count();
		assertEquals(nBefore.longValue()+1, nAfter.longValue());
		assertEquals(HttpStatus.OK, out.getStatusCode());
		assertTrue(out.getBody().contains(newRole));
		assertTrue(all.stream().anyMatch(r -> r.getRoleName().equals(newRole)));
	}
	
	@Test
	public void newRole_POST_SUCCESS_FAIL_AFTER_TRY_AGAIN() {
		Long nBefore = logHandlerRepo.count();
		
		String newRole = UUID.randomUUID().toString();
		RolePresenter role = new RolePresenter(newRole, null, null);
		HttpEntity<RolePresenter> httpEntity = new HttpEntity<>(role);
		
		ResponseEntity<String> out = testRestTemplate.exchange("/v1/role/new", HttpMethod.POST, httpEntity, String.class);
		Long nAfter = logHandlerRepo.count();
		ResponseEntity<String> out2 = testRestTemplate.exchange("/v1/role/new", HttpMethod.POST, httpEntity, String.class);
		Long nAfter2 = logHandlerRepo.count();
		
		List<RoleEntity> all = roleEntityRepo.findAll();
		
		assertEquals(nBefore.longValue()+1, nAfter.longValue());
		assertEquals(nBefore.longValue()+2, nAfter2.longValue());
		assertEquals(HttpStatus.OK, out.getStatusCode());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, out2.getStatusCode());
		assertTrue(out.getBody().contains(newRole));
		assertTrue(all.stream().anyMatch(r -> r.getRoleName().equals(newRole)));
	}
	
	@Test
	public void newRole_POST_BAD_REQUEST() {
		Long nBefore = logHandlerRepo.count();
	
		RolePresenter role = new RolePresenter(null, null, null);
		HttpEntity<RolePresenter> httpEntity = new HttpEntity<>(role);
		
		ResponseEntity<String> out = testRestTemplate.exchange("/v1/role/new", HttpMethod.POST, httpEntity, String.class);
		
		List<RoleEntity> all = roleEntityRepo.findAll();
		
		Long nAfter = logHandlerRepo.count();
		assertEquals(nBefore.longValue()+1, nAfter.longValue());
		assertEquals(HttpStatus.BAD_REQUEST, out.getStatusCode());
	}
}

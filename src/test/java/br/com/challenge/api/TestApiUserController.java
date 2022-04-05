package br.com.challenge.api;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.assertj.core.util.Lists;
import org.junit.Before;
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

import br.com.challenge.DBContext;
import br.com.challenge.dto.DataPresenter;
import br.com.challenge.dto.UserPresenter;
import br.com.challenge.entities.challenge.Team;
import br.com.challenge.entities.challenge.User;
import br.com.challenge.enums.DefaltRoleEnum;
import br.com.challenge.repositories.challenge.UserRepo;
import br.com.challenge.repositories.handler.LogHandlerRepo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class TestApiUserController {
	
	@Autowired
    private TestRestTemplate testRestTemplate;
	
	@Autowired
	private LogHandlerRepo logHandlerRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private DBContext dbContext;
	
	@Before
	public void setUp() {
		dbContext.runDBContext();
	}
	
	@Test
	public void postGroup_POST_SUCCES_SIMPLE_ENTITIES() {
		Long nBefore = logHandlerRepo.count();
		ResponseEntity<UserPresenter[]> data = testRestTemplate.getForEntity("https://cgjresszgg.execute-api.eu-west-1.amazonaws.com/users/", UserPresenter[].class);
		
		HttpEntity<UserPresenter[]> httpEntity = new HttpEntity(data.getBody());
		
		ResponseEntity<UserPresenter[]> out = testRestTemplate.exchange("/v1/user/list", HttpMethod.POST, httpEntity, UserPresenter[].class);
		
		Long nAfter = logHandlerRepo.count();
		
		List<User> inBD = userRepo.findAll();
		List<UserPresenter> listUsers = Lists.newArrayList(out.getBody());
		
		Stream<UserPresenter> notInDB = listUsers.stream().filter(u -> inBD.stream().noneMatch(i -> i.getId().equals(u.getId())));
		
		
		assertEquals(nBefore.longValue()+1, nAfter.longValue());
		assertEquals(HttpStatus.OK, out.getStatusCode());
		assertEquals(0, notInDB.count());
	}
	
	@Test
	public void postGroup_POST_SUCCES_SIMPLE_ENTITIES_2_TIMES_CANT_CHANGE_NAMES() {
		Long nBefore = logHandlerRepo.count();
		ResponseEntity<UserPresenter[]> data = testRestTemplate.getForEntity("https://cgjresszgg.execute-api.eu-west-1.amazonaws.com/users/", UserPresenter[].class);
		
		HttpEntity<UserPresenter[]> httpEntity = new HttpEntity(data.getBody());
		
		ResponseEntity<UserPresenter[]> out = testRestTemplate.exchange("/v1/user/list", HttpMethod.POST, httpEntity, UserPresenter[].class);
		
		Long nAfter = logHandlerRepo.count();
		List<UserPresenter> newUsers = new ArrayList<>();
		for(UserPresenter u : data.getBody()){
			newUsers.add(new UserPresenter(u.getId(), UUID.randomUUID().toString()));
		}
		httpEntity = new HttpEntity(newUsers);
		
		ResponseEntity<UserPresenter[]> out2 = testRestTemplate.exchange("/v1/user/list", HttpMethod.POST, httpEntity, UserPresenter[].class);
		
		Long nAfter2 = logHandlerRepo.count();
		
		List<User> inBD = userRepo.findAll();
		List<UserPresenter> listUsers = Lists.newArrayList(out.getBody());
		
		Stream<UserPresenter> notInDB = listUsers.stream().filter(u -> inBD.stream().noneMatch(i -> i.getDisplayName().equals(u.getDisplayName())));
		Stream<UserPresenter> notInDB2 = newUsers.stream().filter(u -> inBD.stream().anyMatch(i -> i.getDisplayName().equals(u.getDisplayName())));
		
		assertEquals(nBefore.longValue()+1, nAfter.longValue());
		assertEquals(nBefore.longValue()+2, nAfter2.longValue());
		assertEquals(HttpStatus.OK, out2.getStatusCode());
		assertEquals(0, notInDB.count());
		assertEquals(0, notInDB2.count());
	}
	
	private UserPresenter getNewUser() {
		String id = UUID.randomUUID().toString();
		return userRepo.findById(id).isPresent() ? getNewUser() : new UserPresenter(id, UUID.randomUUID().toString());
	}
	
	@Test
	public void postUser_POST_SUCCES_SIMPLE_ENTITIE() {
		Long nBefore = logHandlerRepo.count();
		
		UserPresenter user = getNewUser();
		HttpEntity<UserPresenter> httpEntity = new HttpEntity(user);
		
		ResponseEntity<UserPresenter> out = testRestTemplate.exchange("/v1/user", HttpMethod.POST, httpEntity, UserPresenter.class);
		
		Long nAfter = logHandlerRepo.count();
		
		assertEquals(nBefore.longValue()+1, nAfter.longValue());
		assertEquals(HttpStatus.OK, out.getStatusCode());
		assertEquals(user, out.getBody());
		assertEquals(user.hashCode(), out.getBody().hashCode());
	}
	
	@Test
	public void postUser_POST_SUCCES_SIMPLE_ENTITIE_WITH_TEAM_ROLE() {
		Long nBefore = logHandlerRepo.count();
		
		List<Team> teams = new ArrayList();
		teams.add(new Team("7a967a98-e7ef-4009-9e87-b500e26f9b45", "TESTE INC", null));
		Map<String, List<Team>> teamRole = new HashMap<>();
		teamRole.put(DefaltRoleEnum.DEVELOPER.getRole(), teams);
		
		UserPresenter user = getNewUser();
		user.setRole(teamRole);
		HttpEntity<UserPresenter> httpEntity = new HttpEntity(user);
		
		ResponseEntity<UserPresenter> out = testRestTemplate.exchange("/v1/user", HttpMethod.POST, httpEntity, UserPresenter.class);
		
		Long nAfter = logHandlerRepo.count();
		
		assertEquals(nBefore.longValue()+1, nAfter.longValue());
		assertEquals(HttpStatus.OK, out.getStatusCode());
		assertEquals(user.toString(), out.getBody().toString());
	}
	
	@Test
	public void getById_GET_SUCCESS() {
		Long nBefore = logHandlerRepo.count();
		Optional<User> use = userRepo.findById("94e43ccd-a638-4df8-8885-a7a1bb384c03");
		ResponseEntity<UserPresenter> out = testRestTemplate.getForEntity("/v1/user/94e43ccd-a638-4df8-8885-a7a1bb384c03", UserPresenter.class);
		
		Long nAfter = logHandlerRepo.count();
		assertEquals(nBefore.longValue(), nAfter.longValue());
		assertEquals(HttpStatus.OK, out.getStatusCode());
		assertEquals("94e43ccd-a638-4df8-8885-a7a1bb384c03", out.getBody().getId());
	}
	
	@Test
	public void getAll_GET_SUCCES() {
		Long nBefore = logHandlerRepo.count();
		
		ResponseEntity<DataPresenter> out = testRestTemplate.getForEntity("/v1/user/all?page=1&size=1", DataPresenter.class);
		
		Long nAfter = logHandlerRepo.count();
		
		List<UserPresenter> data = (List<UserPresenter>) out.getBody().getData();
 		
		assertEquals(nBefore.longValue(), nAfter.longValue());
		assertEquals(HttpStatus.OK, out.getStatusCode());
		assertEquals(1, data.size());
	}
}

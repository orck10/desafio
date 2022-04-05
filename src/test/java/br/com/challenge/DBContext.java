package br.com.challenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import br.com.challenge.entities.challenge.Team;
import br.com.challenge.entities.challenge.User;
import br.com.challenge.repositories.challenge.TeamRepo;
import br.com.challenge.repositories.challenge.UserRepo;

@Configuration
public class DBContext {
	
	@Autowired
	private TeamRepo teamRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	public void runDBContext() {
		runDBContextTeamEntity();
		runDBContextUserEntity();
	}
	
	private void runDBContextTeamEntity() {
		Team team = new Team("7a967a98-e7ef-4009-9e87-b500e26f9b45", "TESTE INC", null);	
		teamRepo.save(team);
	}
	
	private void runDBContextUserEntity() {
		User user = new User("94e43ccd-a638-4df8-8885-a7a1bb384c03", "Any");
		userRepo.save(user);
	}
}

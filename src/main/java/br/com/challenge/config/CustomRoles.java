package br.com.challenge.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.challenge.entities.challenge.RoleEntity;
import br.com.challenge.enums.DefaltRoleEnum;
import br.com.challenge.repositories.challenge.RoleEntityRepo;

@Configuration
public class CustomRoles {
	
	private Logger log = LogManager.getLogger(CustomRoles.class);
	
	@Bean
	public boolean insertDefaultRoles(RoleEntityRepo roleEntityRepo) {
		for(String r : DefaltRoleEnum.valueOfRole()) {
			try {
				RoleEntity role = new RoleEntity(r);
				roleEntityRepo.save(role);
			}catch (Exception e) {
				log.warn("Falha ao tentar incluir Role " + r);
			}
		}
		return true;
	}
}

package br.com.challenge;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ChallengeApplicationTests { 
	
	@Autowired
	private DBContext dbContext;
	
	@Test void contextLoads() { 
		dbContext.runDBContext();
	}
}
 
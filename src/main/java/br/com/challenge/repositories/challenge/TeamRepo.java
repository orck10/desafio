package br.com.challenge.repositories.challenge;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.challenge.entities.challenge.Team;

public interface TeamRepo extends JpaRepository<Team, String>{

}

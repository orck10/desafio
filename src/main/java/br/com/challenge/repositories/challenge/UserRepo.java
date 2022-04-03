package br.com.challenge.repositories.challenge;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.challenge.entities.challenge.User;

public interface UserRepo extends JpaRepository<User, String>{

}

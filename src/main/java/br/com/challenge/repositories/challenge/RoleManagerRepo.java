package br.com.challenge.repositories.challenge;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.challenge.entities.challenge.Role;

public interface RoleManagerRepo extends JpaRepository<Role, BigInteger>{

}

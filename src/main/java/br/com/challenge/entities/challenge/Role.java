package br.com.challenge.entities.challenge;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="role_challenge")
public class Role {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private BigInteger id;
	private String idUser;
	private String idTeam;
	private String role;
	
	public Role() {
	}
	
	public Role(String idUser, String idTeam, String role) {
		super();
		this.idUser = idUser;
		this.idTeam = idTeam;
		this.role = role;
	}

	public String getIdUser() {
		return idUser;
	}

	public String getIdTeam() {
		return idTeam;
	}

	public String getRole() {
		return role;
	}
}

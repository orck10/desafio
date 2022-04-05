package br.com.challenge.entities.challenge;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.gson.Gson;

@Entity(name="role_entity_challenge")
public class RoleEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private BigInteger id;
	
	@Column(unique = true)
	private String roleName;
	
	public RoleEntity() {
		super();
	}
	
	public RoleEntity(String roleName) {
		super();
		this.roleName = roleName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public String toString() {
		return new Gson().toJson(this);
	}
}

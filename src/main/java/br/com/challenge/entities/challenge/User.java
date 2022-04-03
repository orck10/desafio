package br.com.challenge.entities.challenge;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="user_challenge")
public class User {
	
	@Id
	private String id;
	private String displayName;
	
	public User() {}
	
	public User(String id, String displayName) {
		this.setId(id);
		this.setDisplayName(displayName);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}

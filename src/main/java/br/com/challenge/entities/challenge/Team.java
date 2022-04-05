package br.com.challenge.entities.challenge;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.google.gson.Gson;

@Entity(name="team_challenge")
public class Team {
	
	@Id
	private String id;
	private String name;
	
	@OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_lead", nullable = true)
    private User lead;
	
	public Team() {}
	
	public Team(String id, String name, User lead) {
		this.id = id;
		this.name = name;
		this.lead = lead;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public User getLead() {
		return lead;
	}

	public void setLead(User lead) {
		this.lead = lead;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}

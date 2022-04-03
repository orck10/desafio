package br.com.challenge.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.Gson;

import br.com.challenge.entities.challenge.Team;

@JsonInclude(Include.NON_NULL)	
public class UserPresenter {
	
	private String id;
	private String displayName;
	private Map<String, List<Team>> role;
	
	public UserPresenter() {
		
	}
	
	public UserPresenter(String id, String displayName) {
		this.id = id;
		this.displayName = displayName;
	}

	public String getId() {
		return id;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public void setRole(Map<String, List<Team>> role) {
		this.role = role;
	}
	
	public Map<String, List<Team>> getRole(){
		return this.role;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}

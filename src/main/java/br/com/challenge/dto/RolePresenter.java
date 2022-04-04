package br.com.challenge.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.Gson;

@JsonInclude(Include.NON_NULL)	
public class RolePresenter {
	
	private String role;
	private String userId;
	private String teamID;
	
	public RolePresenter() {
		super();
	}
	
	public RolePresenter(String role, String userId, String teamID) {
		super();
		this.role = role;
		this.userId = userId;
		this.teamID = teamID;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUserId() {
		return userId;
	}

	public String getTeamID() {
		return teamID;
	}
	
	public String toString() {
		return new Gson().toJson(this);
	}
}

package br.com.challenge.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.Gson;

import br.com.challenge.entities.challenge.User;

@JsonInclude(Include.NON_NULL)	
public class TeamPresenter {
	
	private String id;
	private String name;
	private User teamLead;
	private Map<String, List<User>> role;
	private String teamLeadId;
	private List<String> teamMemberIds;
	
	public TeamPresenter(String id, String name, User teamLead) {
		this.id = id;
		this.name = name;
		this.teamLead = teamLead;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public Map<String, List<User>> getRole() {
		return role;
	}

	public void setRole(Map<String, List<User>> role) {
		this.role = role;
	}
	
	public User getTeamLead() {
		return teamLead;
	}

	public void setTeamLead(User teamLead) {
		this.teamLead = teamLead;
	}

	public String getTeamLeadId() {
		return teamLeadId;
	}

	public void setTeamLeadId(String teamLeadId) {
		this.teamLeadId = teamLeadId;
	}

	public List<String> getTeamMemberIds() {
		return teamMemberIds;
	}

	public void setTeamMemberIds(List<String> teamMemberIds) {
		this.teamMemberIds = teamMemberIds;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}

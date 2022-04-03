package br.com.challenge.dto.parser;

import br.com.challenge.dto.TeamPresenter;
import br.com.challenge.entities.challenge.Team;

public class TeamPresenterParser {
	private TeamPresenterParser() {};
	
	public static Team presenterToTeam(TeamPresenter presenter){
		return new Team(presenter.getId(), presenter.getName(), presenter.getTeamLead());
	}
	
	public static TeamPresenter teamToPresenter(Team team){
		return new TeamPresenter(team.getId() , team.getName(), team.getLead());
	}
}

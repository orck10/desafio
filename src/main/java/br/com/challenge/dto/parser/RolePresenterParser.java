package br.com.challenge.dto.parser;

import br.com.challenge.dto.RolePresenter;
import br.com.challenge.entities.challenge.Role;
import br.com.challenge.enums.RoleEnum;

public class RolePresenterParser {
	
	private RolePresenterParser() {};
	
	public static Role presenterToRole(RolePresenter presenter){
		
		String role = presenter.getRole() == null || presenter.getRole().isBlank() || presenter.getRole().isEmpty() ?
				RoleEnum.DEVELOPER.toString(): 
				RoleEnum.valueOfRole(presenter.getRole()).toString();
		return new Role(presenter.getUserId(), presenter.getTeamID(), role);
	}
	
	public static RolePresenter RoleToPresenter(Role role){
		return new RolePresenter(RoleEnum.valueOf(role.getRole()).getRole(), role.getIdUser() , role.getIdTeam());
	}
}
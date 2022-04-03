package br.com.challenge.dto.parser;

import br.com.challenge.dto.RolePresenter;
import br.com.challenge.entities.challenge.Role;
import br.com.challenge.enums.DefaltRoleEnum;

public class RolePresenterParser {
	
	private RolePresenterParser() {};
	
	public static Role presenterToRole(RolePresenter presenter){
		
		String role = presenter.getRole() == null || presenter.getRole().isBlank() || presenter.getRole().isEmpty() ?
				DefaltRoleEnum.DEVELOPER.toString(): 
				presenter.getRole();
		return new Role(presenter.getUserId(), presenter.getTeamID(), role);
	}
	
	public static RolePresenter RoleToPresenter(Role role){
		return new RolePresenter(role.getRole(), role.getIdUser() , role.getIdTeam());
	}
}
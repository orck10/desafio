package br.com.challenge.dto.parser;

import br.com.challenge.dto.UserPresenter;
import br.com.challenge.entities.challenge.User;

public class UserPresenterParser {
	
	private UserPresenterParser() {};
	
	public static User presenterToUser(UserPresenter presenter){
		return new User(presenter.getId(), presenter.getDisplayName());
	}
	
	public static UserPresenter userToPresenter(User user){
		return new UserPresenter(user.getId() , user.getDisplayName());
	}
}

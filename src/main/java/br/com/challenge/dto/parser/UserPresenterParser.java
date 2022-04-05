package br.com.challenge.dto.parser;

import br.com.challenge.dto.UserPresenter;
import br.com.challenge.entities.challenge.User;

public class UserPresenterParser {
	
	private UserPresenterParser() {};
	
	public static User presenterToUser(UserPresenter presenter){
		return new User(presenter.getId(), 
				presenter.getDisplayName(), 
				presenter.getFirstName(), 
				presenter.getLastName(), 
				presenter.getAvatarUrl(), 
				presenter.getLocation());
	}
	
	public static UserPresenter userToPresenter(User user){
		return new UserPresenter(user.getId(), 
				user.getDisplayName(), 
				user.getFirstName(), 
				user.getLastName(), 
				user.getAvatarUrl(), 
				user.getLocation());
	}
}

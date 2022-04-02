package br.com.challenge.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.challenge.entities.User;

@RequestMapping("/v1/user")
public class UserControler {
	
	public ResponseEntity<List<User>> postGroup(){
		return null;
	}
}

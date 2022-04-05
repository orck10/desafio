package br.com.challenge.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.challenge.dto.DataPresenter;
import br.com.challenge.dto.UserPresenter;
import br.com.challenge.services.ServiceEntities;
import br.com.challenge.services.ServiceRequestLog;
import io.swagger.v3.oas.models.PathItem.HttpMethod;

@RestController
@RequestMapping("/v1/user")
public class UserController {
	
	@Autowired
	private ServiceEntities<UserPresenter> service;
	
	@Autowired
	private ServiceRequestLog requestLog;
	
	@Autowired
	private HttpServletRequest request;
	
	@PostMapping(path = "/list")
	public ResponseEntity<List<UserPresenter>> postGroup(@RequestBody List<UserPresenter> users){
		requestLog.logRequest(HttpMethod.POST.toString(), request.getRequestURI(), users.toString());
		return service.insertList(users);
	}
	
	@PostMapping(path = "")
	public ResponseEntity<UserPresenter> postUser(@RequestBody UserPresenter user){
		requestLog.logRequest(HttpMethod.POST.toString(), request.getRequestURI(), user.toString());
		return service.insertInsertOne(user);
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<UserPresenter> getById(@PathVariable("id") String id){
		return service.getEntity(id);
	}
	
	@GetMapping(path = "/all")
	public ResponseEntity<DataPresenter> getAll(@PathParam("page") Integer page, @PathParam("size") Integer size){
		return service.getAll(page, size);
	}
	
}

package br.com.challenge.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.challenge.dto.DataPresenter;
import br.com.challenge.dto.TeamPresenter;
import br.com.challenge.services.ServiceEntities;
import br.com.challenge.services.ServiceRequestLog;
import io.swagger.v3.oas.models.PathItem.HttpMethod;

@RestController
@RequestMapping("/v1/team")
public class TeamController {
	
	@Autowired
	private ServiceEntities<TeamPresenter> service;
	
	@Autowired
	private ServiceRequestLog requestLog;
	
	@Autowired
	private HttpServletRequest request;
	
	@PostMapping(path = "/list")
	public ResponseEntity<List<TeamPresenter>> postGroup(@RequestBody List<TeamPresenter> teams){
		requestLog.logRequest(HttpMethod.POST.toString(), request.getRequestURI(), teams.toString());
		return service.insertList(teams);
	}
	
	@PostMapping(path = "")
	public ResponseEntity<TeamPresenter> post(@RequestBody TeamPresenter team){
		requestLog.logRequest(HttpMethod.POST.toString(), request.getRequestURI(), team.toString());
		return service.insertInsertOne(team);
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<TeamPresenter> getById(@PathVariable("id") String id){
		return service.getEntity(id);
	}
	
	@GetMapping(path = "/all")
	public ResponseEntity<DataPresenter> getAll(@PathParam("page") Integer page, @PathParam("size") Integer size){
		return service.getAll(page, size);
	}
	
	@PutMapping(path = "/set/lead")
	public ResponseEntity<TeamPresenter> setLead(@PathParam("userid") String userid, @PathParam("teamid") String teamid){
		String uri = request.getRequestURI()+"?userid="+userid+"&teamid="+teamid;
		requestLog.logRequest(HttpMethod.PUT.toString(), uri, "");
		return service.setTeamLead(userid, teamid);
	}
}

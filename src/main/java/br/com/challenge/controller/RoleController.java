package br.com.challenge.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.challenge.dto.RolePresenter;
import br.com.challenge.services.ServiceRequestLog;
import br.com.challenge.services.ServiceRole;
import io.swagger.v3.oas.models.PathItem.HttpMethod;

@RestController
@RequestMapping("/v1/role")
public class RoleController {
	@Autowired
	private ServiceRole<RolePresenter> service;
	
	@Autowired
	private ServiceRequestLog requestLog;
	
	@Autowired
	private HttpServletRequest request;
	
	@PostMapping(path = "/")
	public ResponseEntity<RolePresenter> putRope(@RequestBody RolePresenter entity){
		requestLog.logRequest(HttpMethod.POST.toString(), request.getRequestURI(), entity.toString());
		return service.putRole(entity);
	}
}

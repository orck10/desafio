package br.com.challenge.services.impl;

import java.time.LocalDateTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.challenge.entities.handler.LogHandler;
import br.com.challenge.repositories.handler.LogHandlerRepo;
import br.com.challenge.services.ServiceRequestLog;

@Service
public class ServiceRequestLogMongo implements ServiceRequestLog{
	
	@Autowired
	private LogHandlerRepo repo;
	
	private Logger log = LogManager.getLogger(ServiceRequestLogMongo.class);
	
	@Override
	public void logRequest(String method, String endPoint, String data) {
		LogHandler logEntity = new LogHandler(method, endPoint, data, LocalDateTime.now());
		log.info(logEntity.toString());
		repo.save(logEntity);
	}

}

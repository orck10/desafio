package br.com.challenge.services.impl;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.challenge.entities.handler.LogHandler;
import br.com.challenge.repositories.handler.LogHandlerRepo;

@RunWith(MockitoJUnitRunner.class)
public class TestServiceRequestLogMongo {
	
	@Mock
	private LogHandlerRepo repo;
	
	@InjectMocks
	private ServiceRequestLogMongo serviceRequestLogMongo;
	
	@Test
	public void logRequest() {
		when(repo.save(Mockito.any())).thenReturn(new LogHandler("", "", "", LocalDateTime.now()));
		serviceRequestLogMongo.logRequest("", "", "");
		verify(repo, atLeastOnce()).save(Mockito.any());
	}
}

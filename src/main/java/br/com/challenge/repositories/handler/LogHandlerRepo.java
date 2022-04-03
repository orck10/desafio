package br.com.challenge.repositories.handler;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.challenge.entities.handler.LogHandler;

public interface LogHandlerRepo extends MongoRepository<LogHandler, String>{

}

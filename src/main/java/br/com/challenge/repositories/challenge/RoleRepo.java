package br.com.challenge.repositories.challenge;

import java.util.Optional;

import br.com.challenge.dto.DataPresenter;

public interface RoleRepo<T, Y> {
	Optional<T> findEntity(Y entity);
	
	Optional<T> insert(T dto);
	
	DataPresenter findAllWithPag(Integer page, Integer size);
}

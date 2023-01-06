package com.algaworks.algafood.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

//5.20. Estendendo o JpaRepository para customizar o repositório base - 3'30"

@NoRepositoryBean //Essa anotação diz para o Spring Data JPA não instanciar uma implementação para essa interface
public interface CustomJpaRepository<T, ID> extends JpaRepository<T, ID>{

	Optional<T> buscarPrimeiro();
	
	//12.11. Implementando regra de negócio para evitar usuários com e-mails duplicados - 12'30"
	void detach(T entity);
}

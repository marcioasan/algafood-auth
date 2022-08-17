package com.algaworks.algafood.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Cozinha;

//5.3. Criando um repositório com Spring Data JPA (SDJ)
@Repository
public interface CozinhaRepository extends JpaRepository<Cozinha, Long>{

//	List<Cozinha> consultarPorNome(String nome);
}

package com.algaworks.algafood.domain.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Usuario;

@Repository
public interface UsuarioRepository extends CustomJpaRepository<Usuario, Long>{

	//12.11. Implementando regra de negócio para evitar usuários com e-mails duplicados - 1'
	Optional<Usuario> findByEmail(String email);
}

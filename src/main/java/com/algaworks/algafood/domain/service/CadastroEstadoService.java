package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;

@Service
public class CadastroEstadoService {

	private static final String MSG_ESTADO_EM_USO  = "Estado de código %d não pode ser removido, pois está em uso";	
	
	@Autowired
	private EstadoRepository estadoRepository;

	@Transactional //11.1. Analisando e definindo melhor o escopo das transações - 3'30"
	public Estado salvar(Estado estado) {
		return estadoRepository.save(estado);
	}
	
	@Transactional //11.1. Analisando e definindo melhor o escopo das transações - 3'30"
	public void excluir(Long estadoId) {
		System.out.println("***** DELETANDO ESTADO *****");
		try {
			estadoRepository.deleteById(estadoId);
			estadoRepository.flush(); //11.21. Corrigindo bug de tratamento de exception de integridade de dados com flush do JPA - 6'45" - para corrigir o erro DataIntegrityViolationException
		} catch (EmptyResultDataAccessException e) {
			throw new EstadoNaoEncontradoException(estadoId);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_ESTADO_EM_USO, estadoId));
		}		
	}

	public Estado buscarOuFalhar(Long estadoId) {		
		return estadoRepository.findById(estadoId)
				.orElseThrow(() -> new EstadoNaoEncontradoException(estadoId));
	}
	
}

package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

//4.27. Implementando a camada de domain services (e a importância da linguagem ubíqua)

@Service
public class CadastroCozinhaService {

	private static final String MSG_COZINHA_EM_USO = "Cozinha de código %d não pode ser removida, pois está em uso";
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Transactional //11.1. Analisando e definindo melhor o escopo das transações - 3'30", 4'10"
	public Cozinha salvar(Cozinha cozinha) {
		return cozinhaRepository.save(cozinha);
	}
	
	//4.28. Refatorando a exclusão de cozinhas para usar domain services - 4'
	@Transactional //11.1. Analisando e definindo melhor o escopo das transações - 3'30"
	public void excluir(Long cozinhaId) {
		try {
			cozinhaRepository.deleteById(cozinhaId);
			
			cozinhaRepository.flush();//11.21. Corrigindo bug de tratamento de exception de integridade de dados com flush do JPA - 6'45" - para corrigir o erro DataIntegrityViolationException
			
		} catch (EmptyResultDataAccessException e) {
			throw new CozinhaNaoEncontradaException(cozinhaId);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_COZINHA_EM_USO, cozinhaId));
		}
	}
	
	//8.5. Simplificando o código com o uso de @ResponseStatus em exceptions - 5'40"
	public Cozinha buscarOuFalhar(Long cozinhaId) {
		return cozinhaRepository.findById(cozinhaId)
				.orElseThrow(() -> new CozinhaNaoEncontradaException(cozinhaId));
	}
	
	/*
	//8.3. Lançando exceções do tipo ResponseStatusException - 1', 3'20"
	public void excluir(Long cozinhaId) {
		try {
			cozinhaRepository.deleteById(cozinhaId);			
		} catch (EmptyResultDataAccessException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
					String.format("Não existe um cadastro de cozinha com código %d", cozinhaId)); //Exception  base introduzida a partir do Spring 5
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format("Cozinha de código %d não pode ser removida, pois está em uso", cozinhaId));
		}
	}
	*/
}

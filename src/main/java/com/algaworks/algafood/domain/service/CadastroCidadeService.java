package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.CidadeRepository;

@Service
public class CadastroCidadeService {

	private static final String MSG_CIDADE_EM_USO = "Cidade de código %d não pode ser removida, pois está em uso";	
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CadastroEstadoService cadastroEstado;
	
	@Transactional //11.1. Analisando e definindo melhor o escopo das transações - 3'30"
	public Cidade salvar(Cidade cidade) {
	    Long estadoId = cidade.getEstado().getId();
	    Estado estado = cadastroEstado.buscarOuFalhar(estadoId);
	    cidade.setEstado(estado);
	    return cidadeRepository.save(cidade);
	}
	
	@Transactional //11.1. Analisando e definindo melhor o escopo das transações - 3'30"
	public void excluir(Long cidadeId) {
		System.out.println("***** DELETANDO CIDADE *****");
		try {
			cidadeRepository.deleteById(cidadeId);			
		} catch (EmptyResultDataAccessException e) {
			throw new CidadeNaoEncontradaException(cidadeId);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_CIDADE_EM_USO, cidadeId));
		}		
	}

	public Cidade buscarOuFalhar(Long cidadeId) {
		return cidadeRepository.findById(cidadeId)
		.orElseThrow(() -> new CidadeNaoEncontradaException(cidadeId));		
	}
}

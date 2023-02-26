package com.algaworks.algafood.domain.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.repository.ProdutoRepository;

//14.6. Implementando serviço de cadastro de foto de produto - 1'

@Service
public class CatalogoFotoProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	//14.7. Excluindo e substituindo cadastro de foto de produto - 1'
	@Transactional
	public FotoProduto salvar(FotoProduto foto) {
		Long restauranteId = foto.getRestauranteId();
		Long produtoId = foto.getProduto().getId();
		
		Optional<FotoProduto> fotoExistente = produtoRepository
				.findFotoById(restauranteId, produtoId);
		
		if (fotoExistente.isPresent()) {
			produtoRepository.delete(fotoExistente.get());
		}
		
		return produtoRepository.save(foto);
	}
	
}

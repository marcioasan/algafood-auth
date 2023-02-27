package com.algaworks.algafood.domain.service;

import java.io.InputStream;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.FotoProdutoNaoEncontradaException;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import com.algaworks.algafood.domain.service.FotoStorageService.NovaFoto;

//14.6. Implementando serviço de cadastro de foto de produto - 1'

@Service
public class CatalogoFotoProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private FotoStorageService fotoStorage;
	
	//14.7. Excluindo e substituindo cadastro de foto de produto - 1'
	//14.9. Integrando o serviço de catálogo de fotos com o serviço de armazenagem - 1'30", 6'
	@Transactional
	public FotoProduto salvar(FotoProduto foto, InputStream dadosArquivo) {
		Long restauranteId = foto.getRestauranteId();
		Long produtoId = foto.getProduto().getId();
		String nomeNovoArquivo = fotoStorage.gerarNomeArquivo(foto.getNomeArquivo());
		String nomeArquivoExistente = null;
		
		Optional<FotoProduto> fotoExistente = produtoRepository
				.findFotoById(restauranteId, produtoId);
		
		if (fotoExistente.isPresent()) {
			nomeArquivoExistente = fotoExistente.get().getNomeArquivo();
			produtoRepository.delete(fotoExistente.get());
		}
		
		foto.setNomeArquivo(nomeNovoArquivo);
		foto =  produtoRepository.save(foto);
		produtoRepository.flush();
		
		NovaFoto novaFoto = NovaFoto.builder()
				.nomeAquivo(foto.getNomeArquivo())
				.inputStream(dadosArquivo)
				.build();

		fotoStorage.substituir(nomeArquivoExistente, novaFoto);
		
		return foto;
	}

	public FotoProduto buscarOuFalhar(Long restauranteId, Long produtoId) {
	    return produtoRepository.findFotoById(restauranteId, produtoId)
	            .orElseThrow(() -> new FotoProdutoNaoEncontradaException(restauranteId, produtoId));
	}
}

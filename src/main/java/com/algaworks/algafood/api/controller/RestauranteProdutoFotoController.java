package com.algaworks.algafood.api.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.algafood.api.assembler.FotoProdutoModelAssembler;
import com.algaworks.algafood.api.model.FotoProdutoModel;
import com.algaworks.algafood.api.model.input.FotoProdutoInput;
import com.algaworks.algafood.api.model.input.MultipleFotoProdutoInput;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.service.CadastroProdutoService;
import com.algaworks.algafood.domain.service.CatalogoFotoProdutoService;

//14.2. Implementando upload de arquivo com multipart/form-data - 1'
//14.6. Implementando serviço de cadastro de foto de produto - 6'50"

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {

	@Autowired
	private CadastroProdutoService cadastroProduto;
	
	@Autowired
	private CatalogoFotoProdutoService catalogoFotoProduto;
	
	@Autowired
	private FotoProdutoModelAssembler fotoProdutoModelAssembler;
	
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public FotoProdutoModel atualizarFoto(@PathVariable Long restauranteId,
			@PathVariable Long produtoId, @Valid FotoProdutoInput fotoProdutoInput) throws IOException {
		Produto produto = cadastroProduto.buscarOuFalhar(restauranteId, produtoId);
		
		MultipartFile arquivo = fotoProdutoInput.getArquivo();
		
		FotoProduto foto = new FotoProduto();
		foto.setProduto(produto);
		foto.setDescricao(fotoProdutoInput.getDescricao());
		foto.setContentType(arquivo.getContentType());
		foto.setTamanho(arquivo.getSize());
		foto.setNomeArquivo(arquivo.getOriginalFilename());
		
		FotoProduto fotoSalva = catalogoFotoProduto.salvar(foto, arquivo.getInputStream());
		
		return fotoProdutoModelAssembler.toModel(fotoSalva);
	}
	
	//14.12. Desafio: Implementando endpoint de consulta de foto de produto
	@GetMapping
	public FotoProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
	    FotoProduto fotoProduto = catalogoFotoProduto.buscarOuFalhar(restauranteId, produtoId);
	    
	    return fotoProdutoModelAssembler.toModel(fotoProduto);
	}
	
	/*
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void atualizarFoto(@PathVariable Long restauranteId,
			@PathVariable Long produtoId, @Valid FotoProdutoInput fotoProdutoInput) {
		
		var nomeArquivo = UUID.randomUUID().toString() 
				+ "_" + fotoProdutoInput.getArquivo().getOriginalFilename();
		
		var arquivoFoto = Path.of("D:\\Marcio\\Algaworks\\ESPECIALISTA SPRING REST\\Fotos\\catalogo", nomeArquivo);
		
		System.out.println(fotoProdutoInput.getDescricao());
		System.out.println(arquivoFoto);
		System.out.println(fotoProdutoInput.getArquivo().getContentType());
		
		try {
			fotoProdutoInput.getArquivo().transferTo(arquivoFoto);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	*/
	
	@PutMapping(value = "/batch-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void atualizarMultiplasFotos(@PathVariable Long restauranteId, @PathVariable Long produtoId, 
			MultipleFotoProdutoInput fotos) throws IOException {

		for (FotoProdutoInput foto : fotos.getFotos()) {
			var nomeArquivo = UUID.randomUUID().toString() 
			+ "_" + foto.getArquivo().getOriginalFilename();
	
			var arquivoFoto = Path.of("D:\\Marcio\\Algaworks\\ESPECIALISTA SPRING REST\\Fotos\\catalogo", nomeArquivo);
			
			System.out.println(foto.getDescricao());
			System.out.println(arquivoFoto);
			System.out.println(foto.getArquivo().getContentType());
			
			try {
				foto.getArquivo().transferTo(arquivoFoto);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}				
	}
	
}

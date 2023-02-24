package com.algaworks.algafood.api.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.model.input.FotoProdutoInput;
import com.algaworks.algafood.api.model.input.MultipleFotoProdutoInput;

//14.2. Implementando upload de arquivo com multipart/form-data - 1'

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {

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

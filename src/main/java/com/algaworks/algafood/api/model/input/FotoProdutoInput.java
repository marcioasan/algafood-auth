package com.algaworks.algafood.api.model.input;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

//14.2. Implementando upload de arquivo com multipart/form-data - 

@Getter
@Setter
public class FotoProdutoInput {
	
	private MultipartFile arquivo;
	private String descricao;
}

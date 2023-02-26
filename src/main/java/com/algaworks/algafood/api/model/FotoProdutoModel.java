package com.algaworks.algafood.api.model;

import lombok.Getter;
import lombok.Setter;

//14.6. Implementando serviço de cadastro de foto de produto - 10'20"
@Setter
@Getter
public class FotoProdutoModel {

	private String nomeArquivo;
	private String descricao;
	private String contentType;
	private Long tamanho;
	
}

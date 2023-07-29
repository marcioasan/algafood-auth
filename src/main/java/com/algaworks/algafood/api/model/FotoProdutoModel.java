package com.algaworks.algafood.api.model;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;
import lombok.Setter;

//14.6. Implementando serviço de cadastro de foto de produto - 10'20"
@Relation(collectionRelation = "fotos")
@Setter
@Getter
public class FotoProdutoModel extends RepresentationModel<FotoProdutoModel> {

	private String nomeArquivo;
	private String descricao;
	private String contentType;
	private Long tamanho;
	
}

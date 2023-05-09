package com.algaworks.algafood.api.model;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.algaworks.algafood.api.model.view.RestauranteView;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.Setter;

//19.15. Adicionando hypermedia em recursos com paginação
//11.9. Isolando o Domain Model do Representation Model com o padrão DTO

@Relation(collectionRelation = "cozinhas")
@Setter
@Getter
public class CozinhaModel extends RepresentationModel<CozinhaModel>{

	@JsonView(RestauranteView.Resumo.class) //13.1. Fazendo projeção de recursos com @JsonView do Jackson - 4'30"
	private Long id;
	
	@JsonView(RestauranteView.Resumo.class)
	private String nome;
	
}
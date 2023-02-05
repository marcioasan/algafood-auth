package com.algaworks.algafood.api.model;

import com.algaworks.algafood.api.model.view.RestauranteView;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.Setter;

//11.9. Isolando o Domain Model do Representation Model com o padrão DTO

@Setter
@Getter
public class CozinhaModel {

	@JsonView(RestauranteView.Resumo.class) //13.1. Fazendo projeção de recursos com @JsonView do Jackson - 4'30"
	private Long id;
	
	@JsonView(RestauranteView.Resumo.class)
	private String nome;
	
}
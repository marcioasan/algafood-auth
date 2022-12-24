package com.algaworks.algafood.api.model;

import lombok.Getter;
import lombok.Setter;

//11.9. Isolando o Domain Model do Representation Model com o padrão DTO

@Setter
@Getter
public class CozinhaModel {

	private Long id;
	private String nome;
	
}
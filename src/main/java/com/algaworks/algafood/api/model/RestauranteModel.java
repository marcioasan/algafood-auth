package com.algaworks.algafood.api.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

//11.12. Refatorando e criando um assembler de DTO - Classe excluída nessa aula
//11.9. Isolando o Domain Model do Representation Model com o padrão DTO

@Setter
@Getter
public class RestauranteModel {

	private Long id;
	private String nome;
	private BigDecimal taxaFrete;
	private CozinhaModel cozinha;
	
}
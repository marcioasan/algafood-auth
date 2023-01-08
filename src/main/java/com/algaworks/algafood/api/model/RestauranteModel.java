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
	private Boolean ativo; //12.4. Implementando os endpoints de ativação e inativação de restaurantes - 6'20"
	private EnderecoModel endereco; //12.6. Adicionando endereço no modelo da representação do recurso de restaurante - 2'10"
	private Boolean aberto;
}
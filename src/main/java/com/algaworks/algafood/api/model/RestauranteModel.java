package com.algaworks.algafood.api.model;

import java.math.BigDecimal;

import com.algaworks.algafood.api.model.view.RestauranteView;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.Setter;

//11.12. Refatorando e criando um assembler de DTO - Classe excluída nessa aula
//11.9. Isolando o Domain Model do Representation Model com o padrão DTO

@Setter
@Getter
public class RestauranteModel {
	
	@JsonView({ RestauranteView.Resumo.class, RestauranteView.ApenasNome.class}) //13.1. Fazendo projeção de recursos com @JsonView do Jackson - 2'30", 8'15"
	private Long id;
	
	@JsonView({ RestauranteView.Resumo.class, RestauranteView.ApenasNome.class})
	private String nome;
	
	@JsonView(RestauranteView.Resumo.class)
	private BigDecimal taxaFrete;
	
	@JsonView(RestauranteView.Resumo.class)
	private CozinhaModel cozinha;
	
	private Boolean ativo; //12.4. Implementando os endpoints de ativação e inativação de restaurantes - 6'20"
	private EnderecoModel endereco; //12.6. Adicionando endereço no modelo da representação do recurso de restaurante - 2'10"
	private Boolean aberto;
}
package com.algaworks.algafood.api.v1.model.input;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

//11.11. Criando DTOs para entrada de dados na API

@Setter
@Getter
public class CozinhaIdInput {
	
	@NotNull
	private Long id;
}

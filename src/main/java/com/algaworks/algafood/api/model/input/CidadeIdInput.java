package com.algaworks.algafood.api.model.input;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

//12.7. Refatorando serviço de cadastro de restaurante para incluir endereço - 2'

@Setter
@Getter
public class CidadeIdInput {

	@NotNull
	private Long id;
}

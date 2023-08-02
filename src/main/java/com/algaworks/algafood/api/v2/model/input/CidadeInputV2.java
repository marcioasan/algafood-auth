package com.algaworks.algafood.api.v2.model.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

//20.11. Implementando o versionamento da API por Media Type - 10'15"

@Setter
@Getter
public class CidadeInputV2 {

	@NotBlank
	private String nomeCidade;
	
	@NotNull
	private Long idEstado;
	
}

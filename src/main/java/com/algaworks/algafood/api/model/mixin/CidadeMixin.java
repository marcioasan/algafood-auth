package com.algaworks.algafood.api.model.mixin;

import com.algaworks.algafood.domain.model.Estado;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//11.4. Desafio: usando @JsonIgnoreProperties e Jackson Mixin
public class CidadeMixin {

	@JsonIgnoreProperties(value = "nome", allowGetters = true)
	private Estado estado;
}

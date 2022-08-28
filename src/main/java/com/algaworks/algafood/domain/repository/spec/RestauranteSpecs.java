package com.algaworks.algafood.domain.repository.spec;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.algaworks.algafood.domain.model.Restaurante;

//5.18. Criando uma fábrica de Specifications
public class RestauranteSpecs {
	
	public static Specification<Restaurante> comFreteGratis() {
		return (root, query, builder) -> 
			builder.equal(root.get("taxaFrete"), BigDecimal.ZERO);
	}
	
	public static Specification<Restaurante> comNomeSemelhante(String nome) {
		return (root, query, builder) ->
			builder.like(root.get("nome"), "%" + nome + "%");
	}
}


//Substituição da função Lambda
//https://app.algaworks.com/forum/topicos/82731/substituicao-da-funcao-lambda
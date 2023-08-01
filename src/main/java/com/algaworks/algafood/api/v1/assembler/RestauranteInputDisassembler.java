package com.algaworks.algafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.model.input.RestauranteInput;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;

//11.13. Desafio: Refatorando e criando um disassembler do DTO

@Component
public class RestauranteInputDisassembler {

	@Autowired
	private ModelMapper modelMapper;
	
	//11.14. Adicionando e usando o ModelMapper - 6'50"
	public Restaurante toDomainObject(RestauranteInput restauranteInput) {
		return modelMapper.map(restauranteInput, Restaurante.class);
	}
	
	//11.17. Mapeando para uma instância destino (e não um tipo) com ModelMapper - 1'20"
	public void copyToDomainObject(RestauranteInput restauranteInput, Restaurante restaurante) {
		// Para evitar org.hibernate.HibernateException: identifier of an instance of 
		// com.algaworks.algafood.domain.model.Cozinha was altered from 1 to 2
		//*** Comentário interessante de um aluno sobre essa solução --> https://app.algaworks.com/forum/topicos/87944/sobre-a-solucao-do-new-cozinha  Open-Session-In-View
		restaurante.setCozinha(new Cozinha());
		
		//Endereco - 12.7. Refatorando servico de cadastro de restaurante para incluir endereco - 11'10"
		if (restaurante.getEndereco() != null) {
			restaurante.getEndereco().setCidade(new Cidade());
		}
		
		modelMapper.map(restauranteInput, restaurante);
	}
	
	//11.11. Criando DTOs para entrada de dados na API - 13'
	/*
	public Restaurante toDomainObject(RestauranteInput restauranteInput) {
		Restaurante restaurante = new Restaurante();
		restaurante.setNome(restauranteInput.getNome());
		restaurante.setTaxaFrete(restauranteInput.getTaxaFrete());
		
		Cozinha cozinha = new Cozinha();
		cozinha.setId(restauranteInput.getCozinha().getId());
		
		restaurante.setCozinha(cozinha);
		
		return restaurante;
	}
	*/
}

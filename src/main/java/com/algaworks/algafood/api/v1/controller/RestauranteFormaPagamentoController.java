package com.algaworks.algafood.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.assembler.FormaPagamentoModelAssembler;
import com.algaworks.algafood.api.v1.model.FormaPagamentoModel;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

//12.12. Implementando os endpoints de associação de formas de pagamento em restaurantes - 2'30"

@RestController
@RequestMapping(value = "/v1/restaurantes/{restauranteId}/formas-pagamento")
public class RestauranteFormaPagamentoController {

	@Autowired
	private CadastroRestauranteService cadastroRestaurante;
	
	@Autowired
	private FormaPagamentoModelAssembler formaPagamentoModelAssembler;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	//19.28. Adicionando links para desassociação de formas de pagamento com restaurante
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public CollectionModel<FormaPagamentoModel> listar(@PathVariable Long restauranteId) {
		Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);

		CollectionModel<FormaPagamentoModel> formasPagamentoModel
				= formaPagamentoModelAssembler.toCollectionModel(restaurante.getFormasPagamento())
				.removeLinks()
				.add(algaLinks.linkToRestauranteFormasPagamento(restauranteId))
				.add(algaLinks.linkToRestauranteFormaPagamentoAssociacao(restauranteId, "associar"));

		formasPagamentoModel.getContent().forEach(formaPagamentoModel -> {
			formaPagamentoModel.add(algaLinks.linkToRestauranteFormaPagamentoDesassociacao(
					restauranteId, formaPagamentoModel.getId(), "desassociar"));
		});

		return formasPagamentoModel;
	}
	
//	@Override
//	@GetMapping
//	public CollectionModel<FormaPagamentoModel> listar(@PathVariable Long restauranteId) {
//	    Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);
//	    
//	    return formaPagamentoModelAssembler.toCollectionModel(restaurante.getFormasPagamento())
//	            .removeLinks()
//	            .add(algaLinks.linkToRestauranteFormasPagamento(restauranteId));
//	}
	
//	@GetMapping
//	public List<FormaPagamentoModel> listar(@PathVariable Long restauranteId) {
//		Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);
//		
//		return formaPagamentoModelAssembler.toCollectionModel(restaurante.getFormasPagamento());
//	}

	//19.28. Adicionando links para desassociação de formas de pagamento com restaurante
	@DeleteMapping("/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> desassociar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
		cadastroRestaurante.desassociarFormaPagamento(restauranteId, formaPagamentoId);

		return ResponseEntity.noContent().build();
	}
	
	
	
	//12.12. Implementando os endpoints de associação de formas de pagamento em restaurantes - 12'
//	@DeleteMapping("/{formaPagamentoId}")
//	@ResponseStatus(HttpStatus.NO_CONTENT)
//	public void desassociar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
//		cadastroRestaurante.desassociarFormaPagamento(restauranteId, formaPagamentoId);
//	}

	//19.29. Adicionando links com template variable de caminho de formas de pagamento do restaurante
	@PutMapping("/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> associar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
		cadastroRestaurante.associarFormaPagamento(restauranteId, formaPagamentoId);

		return ResponseEntity.noContent().build();
	}
	
	
//	@PutMapping("/{formaPagamentoId}")
//	@ResponseStatus(HttpStatus.NO_CONTENT)
//	public void associar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
//		cadastroRestaurante.associarFormaPagamento(restauranteId, formaPagamentoId);
//	}
}

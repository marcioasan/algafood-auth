package com.algaworks.algafood.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.service.FluxoPedidoService;

//12.22. Implementando endpoint de transição de status de pedidos - 13'30"

@RestController
@RequestMapping(value = "/v1/pedidos/{codigoPedido}") //12.25. Usando IDs vs UUIDs nas URIs de recursos - 17'
public class FluxoPedidoController {

	@Autowired
	private FluxoPedidoService fluxoPedido;
	
	@PutMapping("/confirmacao")
	@ResponseStatus(HttpStatus.NO_CONTENT)
//	public void confirmar(@PathVariable String codigoPedido) {
	public ResponseEntity<Void> confirmar(@PathVariable String codigoPedido) { ////19.22. Adicionando links de transições de status de pedidos - 3'40"
		fluxoPedido.confirmar(codigoPedido);
		return ResponseEntity.noContent().build();
	}

	//12.23. Desafio: implementando endpoints de transição de status de pedidos
	@PutMapping("/cancelamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> cancelar(@PathVariable String codigoPedido) {
		fluxoPedido.cancelar(codigoPedido);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/entrega")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> entregar(@PathVariable String codigoPedido) {
		fluxoPedido.entregar(codigoPedido);
		return ResponseEntity.noContent().build();
	}
}
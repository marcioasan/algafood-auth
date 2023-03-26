package com.algaworks.algafood.domain.event;

import com.algaworks.algafood.domain.model.Pedido;

import lombok.AllArgsConstructor;
import lombok.Getter;

//15.14. Desafio: enviando e-mails no cancelamento de pedidos

@Getter
@AllArgsConstructor
public class PedidoCanceladoEvent {

	private Pedido pedido;
}

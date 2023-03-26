package com.algaworks.algafood.domain.event;

import com.algaworks.algafood.domain.model.Pedido;

import lombok.AllArgsConstructor;
import lombok.Getter;

//15.11. Publicando Domain Events a partir do Aggregate Root - 4' 

@Getter
@AllArgsConstructor
public class PedidoConfirmadoEvent {

	private Pedido pedido;
}

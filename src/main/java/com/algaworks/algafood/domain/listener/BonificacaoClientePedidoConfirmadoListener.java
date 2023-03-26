package com.algaworks.algafood.domain.listener;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.domain.event.PedidoConfirmadoEvent;

//15.12. Observando e reagindo a Domain Events - 5'30" - Classe criada na aula apenas para explicação, o Thiago apagou. 

@Component
public class BonificacaoClientePedidoConfirmadoListener {

	//@Order(1) //indica que é o primeiro evento a ser disparado
	//@EventListener
	public void aoConfirmarPedidoCalcularBonificacao(PedidoConfirmadoEvent event) {
		System.out.println(">>> Calculando pontos para cliente " + event.getPedido().getCliente().getNome());
	}
}

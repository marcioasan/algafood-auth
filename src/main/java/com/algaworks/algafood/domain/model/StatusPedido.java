package com.algaworks.algafood.domain.model;

import java.util.Arrays;
import java.util.List;

//7.12. Desafio: Criando migrações e mapeando as entidades Pedido e ItemPedido
//12.22. Implementando endpoint de transição de status de pedidos - 16'50" - foi colocada a descrição dos status
//12.24. Refatorando o código de regras para transição de status de pedidos - 5'
public enum StatusPedido {

	CRIADO("Criado"),
	CONFIRMADO("Confirmado", CRIADO),
	ENTREGUE("Entregue", CONFIRMADO),
	CANCELADO("Cancelado", CRIADO);
	
	private String descricao;
	private List<StatusPedido> statusAnteriores;
	
	StatusPedido(String descricao, StatusPedido... statusAnteriores) {
		this.descricao = descricao;
		this.statusAnteriores = Arrays.asList(statusAnteriores);
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	public boolean naoPodeAlterarPara(StatusPedido novoStatus) {
		return !novoStatus.statusAnteriores.contains(this); //12.24. Refatorando o código de regras para transição de status de pedidos - 9'15" - this é o status atual
	}

	public boolean podeAlterarPara(StatusPedido novoStatus) {
		return !naoPodeAlterarPara(novoStatus); //19.23. Adicionando links condicionalmente
	}
}

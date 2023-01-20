package com.algaworks.algafood.domain.model;

//7.12. Desafio: Criando migrações e mapeando as entidades Pedido e ItemPedido
//12.22. Implementando endpoint de transição de status de pedidos - 16'50" - foi colocada a descrição dos status

public enum StatusPedido {

	CRIADO("Criado"),
	CONFIRMADO("Confirmado"),
	ENTREGUE("Entregue"),
	CANCELADO("Cancelado");
	
	private String descricao;
	
	StatusPedido(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
}

package com.algaworks.algafood.api.model.input;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

//12.21. Desafio: Implementando o endpoint de emissão de pedidos

@Setter
@Getter
public class PedidoInput {

    @Valid
    @NotNull
    private RestauranteIdInput restaurante;
    
    @Valid
    @NotNull
    private EnderecoInput enderecoEntrega;
    
    @Valid
    @NotNull
    private FormaPagamentoIdInput formaPagamento;
    
    @Valid
    @Size(min = 1) //12.21. Desafio: Implementando o endpoint de emissão de pedidos - 3'
    @NotNull
    private List<ItemPedidoInput> itens;
}

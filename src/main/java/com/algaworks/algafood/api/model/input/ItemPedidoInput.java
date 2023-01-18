package com.algaworks.algafood.api.model.input;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.Getter;
import lombok.Setter;

//12.21. Desafio: Implementando o endpoint de emissão de pedidos

@Setter
@Getter
public class ItemPedidoInput {

    @NotNull
    private Long produtoId;
    
    @NotNull
    @PositiveOrZero
    private Integer quantidade;
    
    private String observacao;
}

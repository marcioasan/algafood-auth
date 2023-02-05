package com.algaworks.algafood.api.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.Getter;
import lombok.Setter;

//12.20. Otimizando a query de pedidos e retornando model resumido na listagem
@JsonFilter("pedidoFilter") //13.2. Limitando os campos retornados pela API com @JsonFilter do Jackson - 3'
@Setter
@Getter
public class PedidoResumoModel {

//    private Long id;
    private String codigo; //12.25. Usando IDs vs UUIDs nas URIs de recursos - 13'20"
    private BigDecimal subtotal;
    private BigDecimal taxaFrete;
    private BigDecimal valorTotal;
    private String status;
    private OffsetDateTime dataCriacao;
    private RestauranteResumoModel restaurante;
    private UsuarioModel cliente; 
}

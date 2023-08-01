package com.algaworks.algafood.api.v1.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;
import lombok.Setter;

//12.20. Otimizando a query de pedidos e retornando model resumido na listagem
//@JsonFilter("pedidoFilter") //13.2. Limitando os campos retornados pela API com @JsonFilter do Jackson - 3' | ***essa anotação foi utilizada apenas nessa aula junto com o método listar que devolve MappingJacksonValue que está comentado em PedidoController, para a aula "13.3. Limitando os campos retornados pela API com Squiggly" é utilizado o método listar da forma como estava antes da aula 13.2
@Relation(collectionRelation = "pedidos")
@Setter
@Getter
public class PedidoResumoModel extends RepresentationModel<PedidoResumoModel> {

	private RestauranteApenasNomeModel restaurante;
//    private Long id;
    private String codigo; //12.25. Usando IDs vs UUIDs nas URIs de recursos - 13'20"
    private BigDecimal subtotal;
    private BigDecimal taxaFrete;
    private BigDecimal valorTotal;
    private String status;
    private OffsetDateTime dataCriacao;
//    private RestauranteResumoModel restaurante;
    private UsuarioModel cliente;
//    private String nomeCliente;
}

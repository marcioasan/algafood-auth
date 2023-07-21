package com.algaworks.algafood.api.model;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;
import lombok.Setter;

//19.24. Desafio: adicionando hypermedia nos recursos de restaurantes
@Relation(collectionRelation = "restaurantes")
@Setter
@Getter
public class RestauranteApenasNomeModel extends RepresentationModel<RestauranteApenasNomeModel> {

    private Long id;
    
    private String nome;
}

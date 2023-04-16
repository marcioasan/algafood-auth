package com.algaworks.algafood.api.model;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;
import lombok.Setter;

@Relation(collectionRelation = "cidades") //19.10. Adicionando hypermedia na representação de recursos de coleção - 11'30"
@Setter
@Getter
public class CidadeModel extends RepresentationModel<CidadeModel> { //19.7. Adicionando hypermedia na representação de recurso único com HAL - 1'

    private Long id;
    private String nome;
    private EstadoModel estado;
}

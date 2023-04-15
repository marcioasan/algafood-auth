package com.algaworks.algafood.api.model;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CidadeModel extends RepresentationModel<CidadeModel> { //19.7. Adicionando hypermedia na representação de recurso único com HAL - 1'

    private Long id;
    private String nome;
    private EstadoModel estado;
}

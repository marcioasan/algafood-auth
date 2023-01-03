package com.algaworks.algafood.api.model;

import lombok.Getter;
import lombok.Setter;

//12.6. Adicionando endereço no modelo da representação do recurso de restaurante - 5'

@Setter
@Getter
public class CidadeResumoModel {

    private Long id;
    private String nome;
    private String estado;
}

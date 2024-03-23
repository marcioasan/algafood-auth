package com.algaworks.algafood.api.v1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.assembler.PermissaoModelAssembler;
import com.algaworks.algafood.api.v1.model.PermissaoModel;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.repository.PermissaoRepository;

//19.34. Desafio: adicionando links de associação de grupos com permissões

@RestController
@RequestMapping(path = "/v1/permissoes", produces = MediaType.APPLICATION_JSON_VALUE)
public class PermissaoController {

    @Autowired
    private PermissaoRepository permissaoRepository;
    
    @Autowired
    private PermissaoModelAssembler permissaoModelAssembler;
    
    @GetMapping
    public CollectionModel<PermissaoModel> listar() {
        List<Permissao> todasPermissoes = permissaoRepository.findAll();
        
        return permissaoModelAssembler.toCollectionModel(todasPermissoes);
    }  
}
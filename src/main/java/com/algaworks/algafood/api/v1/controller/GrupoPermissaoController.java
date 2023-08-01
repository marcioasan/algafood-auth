package com.algaworks.algafood.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.assembler.PermissaoModelAssembler;
import com.algaworks.algafood.api.v1.model.PermissaoModel;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.service.CadastroGrupoService;

//12.15. Desafio: implementando os endpoints de associação de grupos com permissões

@RestController
@RequestMapping(value = "/grupos/{grupoId}/permissoes")
public class GrupoPermissaoController {

	@Autowired
	private CadastroGrupoService cadastroGrupo;
	
	@Autowired
	private PermissaoModelAssembler permissaoModelAssembler;
	
	@Autowired
	private AlgaLinks algaLinks; 
	
	//19.34. Desafio: adicionando links de associação de grupos com permissões
	@GetMapping
	public CollectionModel<PermissaoModel> listar(@PathVariable Long grupoId) {
	    Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);
	    
	    CollectionModel<PermissaoModel> permissoesModel 
	        = permissaoModelAssembler.toCollectionModel(grupo.getPermissoes())
	            .removeLinks()
	            .add(algaLinks.linkToGrupoPermissoes(grupoId))
	            .add(algaLinks.linkToGrupoPermissaoAssociacao(grupoId, "associar"));
	    
	    permissoesModel.getContent().forEach(permissaoModel -> {
	        permissaoModel.add(algaLinks.linkToGrupoPermissaoDesassociacao(
	                grupoId, permissaoModel.getId(), "desassociar"));
	    });
	    
	    return permissoesModel;
	} 
	
	@DeleteMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> desassociar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
	    cadastroGrupo.desassociarPermissao(grupoId, permissaoId);
	    
	    return ResponseEntity.noContent().build();
	}

	@PutMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> associar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
	    cadastroGrupo.associarPermissao(grupoId, permissaoId);
	    
	    return ResponseEntity.noContent().build();
	} 
	
	/*
	@GetMapping
	public List<PermissaoModel> listar(@PathVariable Long grupoId) {
		Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);
		
		return permissaoModelAssembler.toCollectionModel(grupo.getPermissoes());
	}
	
	@DeleteMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void desassociar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		cadastroGrupo.desassociarPermissao(grupoId, permissaoId);
	}
	
	@PutMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void associar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		cadastroGrupo.associarPermissao(grupoId, permissaoId);
	}
	 */
}

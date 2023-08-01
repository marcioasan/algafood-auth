package com.algaworks.algafood.api.v1.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.assembler.EstadoInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.EstadoModelAssembler;
import com.algaworks.algafood.api.v1.model.EstadoModel;
import com.algaworks.algafood.api.v1.model.input.EstadoInput;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.service.CadastroEstadoService;

//19.14. Desafio: adicionando hypermedia nos recursos de estados

@RestController
@RequestMapping("/estados")
public class EstadoController {

	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CadastroEstadoService estadoService;
	
	@Autowired
	private EstadoModelAssembler estadoModelAssembler;

	@Autowired
	private EstadoInputDisassembler estadoInputDisassembler;
	
	
	@GetMapping
	public CollectionModel<EstadoModel> listar() {
	    List<Estado> todosEstados = estadoRepository.findAll();
	    
	    return estadoModelAssembler.toCollectionModel(todosEstados);
	}
	
	
	
	
	//8.6. Desafio: refatorando os serviços REST
	@GetMapping("/{estadoId}")
	public EstadoModel buscar(@PathVariable Long estadoId) {
	    Estado estado = estadoService.buscarOuFalhar(estadoId);
	    
	    return estadoModelAssembler.toModel(estado);
	}
	
	/*
    @GetMapping("/{estadoId}")
	public ResponseEntity<Estado> buscar(@PathVariable Long estadoId) {
		Optional<Estado> estado = estadoRepository.findById(estadoId);
		
		if (estado.isPresent()) {
			return ResponseEntity.ok(estado.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	*/
	
	
	//11.20. Desafio: usando DTOs como representation model
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EstadoModel adicionar(@RequestBody @Valid EstadoInput estadoInput) {
	    Estado estado = estadoInputDisassembler.toDomainObject(estadoInput);
	    
	    estado = estadoService.salvar(estado);
	    
	    return estadoModelAssembler.toModel(estado);
	}
	
    /*
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Estado adicionar(@RequestBody @Valid Estado estado) { //9.9. Desafio: adicionando constraints de validação no modelo - @Valid ver anotações na classe RestauranteController.java		
		estado = estadoService.salvar(estado);
		return estado;
	}
	*/
	
	//11.20. Desafio: usando DTOs como representation model
	@PutMapping("/{estadoId}")
	public EstadoModel atualizar(@PathVariable Long estadoId,
	        @RequestBody @Valid EstadoInput estadoInput) {
	    Estado estadoAtual = estadoService.buscarOuFalhar(estadoId);
	    
	    estadoInputDisassembler.copyToDomainObject(estadoInput, estadoAtual);
	    
	    estadoAtual = estadoService.salvar(estadoAtual);
	    
	    return estadoModelAssembler.toModel(estadoAtual);
	}
	
	
	//8.6. Desafio: refatorando os serviços REST
	/*
	@PutMapping("/{estadoId}")
	public Estado atualizar(@PathVariable Long estadoId, @RequestBody @Valid Estado estado) {
		Estado estadoAtual = estadoService.buscarOuFalhar(estadoId);
		BeanUtils.copyProperties(estado, estadoAtual, "id");//4.25. Modelando e implementando a atualização de recursos com PUT - 9' - O parâmetro "id" será ignorado no copyProperties
		return estadoService.salvar(estadoAtual);
	}
	*/
	
	/*
	@PutMapping("/{estadoId}")
	public ResponseEntity<Estado> atualizar(@PathVariable Long estadoId, @RequestBody Estado estado) {
		System.out.println("***** ATUALIZANDO ESTADO *****");
		Optional<Estado> estadoAtual = estadoRepository.findById(estadoId);
		
		if (estadoAtual.isPresent()) {
			BeanUtils.copyProperties(estado, estadoAtual.get(), "id");//4.25. Modelando e implementando a atualização de recursos com PUT - 9' - O parâmetro "id" será ignorado no copyProperties
			Estado estadoSalvo = estadoService.salvar(estadoAtual.get());
			return ResponseEntity.ok(estadoSalvo);
		}
		return ResponseEntity.notFound().build();
	}
	*/
	
	
	
	//8.6. Desafio: refatorando os serviços REST
	@DeleteMapping("/{estadoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long estadoId) {
		estadoService.excluir(estadoId);
	}
	
	/*
	@DeleteMapping("/{estadoId}")
	public ResponseEntity<?> excluir(@PathVariable Long estadoId) {
		System.out.println("***** ACESSANDO O CONTROLLER DE ESTADO *****");
		try {
			estadoService.excluir(estadoId);
			return ResponseEntity.noContent().build();
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}
	*/
}

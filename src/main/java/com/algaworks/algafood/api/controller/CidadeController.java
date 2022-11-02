package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CadastroCidadeService cidadeService;
	
	@GetMapping
	public List<Cidade> listar() {
		return cidadeRepository.findAll();
	}
	
	
	
	
	
	//8.6. Desafio: refatorando os serviços REST
	@GetMapping("/{cidadeId}")
	public Cidade buscar(@PathVariable Long cidadeId) {
		return cidadeService.buscarOuFalhar(cidadeId);
	}
	
	/*
    @GetMapping("/{cidadeId}")
	public ResponseEntity<Cidade> buscar(@PathVariable Long cidadeId) {
		Optional<Cidade> cidade = cidadeRepository.findById(cidadeId);
		
		if (cidade.isEmpty()) {
			return ResponseEntity.ok(cidade.get());
		}
		
		return ResponseEntity.notFound().build();
	}
    */
	
	
	
	
	
	
	//8.6. Desafio: refatorando os serviços REST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
	public Cidade adicionar(@RequestBody Cidade cidade) {
    	try {
    		Cidade cidadeSalva = cidadeService.salvar(cidade);
    		return cidadeSalva;
		} catch (EstadoNaoEncontradoException e) { //8.8. Criando a exception NegocioException - 10'
			throw new NegocioException(e.getMessage(), e);//8.10. Afinando a granularidade e definindo a hierarquia das exceptions de negócios - 16', 17'10"
		}
    }
	
//	@PostMapping
//	@ResponseStatus(HttpStatus.CREATED)
//	public Cidade adicionar(@RequestBody Cidade cidade) {		
//		cidade = cidadeService.salvar(cidade);
//		return cidade;
//	}
	
    /**
     Duas formas de retornar HttpStatus.CREATED
     1 - anotado no método
     2 - no retorno do método
     */
    
	/*
    @PostMapping
	public ResponseEntity<?> adicionar(@RequestBody Cidade cidade) {
		try {
			cidade = cidadeService.salvar(cidade);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(cidade);
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
    */
	
    
    
    
    
    
    
    //8.6. Desafio: refatorando os serviços REST
    @PutMapping("/{cidadeId}")
    public Cidade atualizar(@PathVariable Long cidadeId, @RequestBody Cidade cidade) {
    	
    	try {
    		Cidade cidadeAtual = cidadeService.buscarOuFalhar(cidadeId);
    		BeanUtils.copyProperties(cidade, cidadeAtual, "id");//4.25. Modelando e implementando a atualização de recursos com PUT - 9' - O parâmetro "id" será ignorado no copyProperties
    		
    		return cidadeService.salvar(cidadeAtual);
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
    }
        
    /*
	@PutMapping("/{cidadeId}")
	public ResponseEntity<Cidade> atualizar(@PathVariable Long cidadeId, @RequestBody Cidade cidade) {		
		Optional<Cidade> cidadeAtual = cidadeRepository.findById(cidadeId);
		
		if (cidadeAtual.isPresent()) {
			BeanUtils.copyProperties(cidade, cidadeAtual.get(), "id");//4.25. Modelando e implementando a atualização de recursos com PUT - 9' - O parâmetro "id" será ignorado no copyProperties
			Cidade cidadeSalva = cidadeService.salvar(cidadeAtual.get());
			return ResponseEntity.ok(cidadeSalva);
		}
		return ResponseEntity.notFound().build();
	}
	*/
    
    
    
    
    
    
    //8.6. Desafio: refatorando os serviços REST
    @DeleteMapping("/{cidadeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long cidadeId) {
    	cidadeService.excluir(cidadeId);
    }    
    
    /*
	@DeleteMapping("/{cidadeId}")
	public ResponseEntity<?> remover(@PathVariable Long cidadeId) {		
		try {
			cidadeService.excluir(cidadeId);
			return ResponseEntity.noContent().build();
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}
    */
}

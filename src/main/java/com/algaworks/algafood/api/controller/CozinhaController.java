package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;

import com.algaworks.algafood.api.model.CozinhasXmlWrapper;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

@RestController
@RequestMapping("/cozinhas") //quando passa só um parâmetro, não precisa da palavra 'value'
//@RequestMapping(value = "/cozinhas", produces = MediaType.APPLICATION_JSON_VALUE)
public class CozinhaController {

	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	//4.13. Implementando content negotiation para retornar JSON ou XML - 6'
	//@GetMapping(produces = {MediaType.APPLICATION_XML_VALUE})
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE) //pode ser colocado no escopo da classe '@RequestMapping'	
	public List<Cozinha> listar() {
		return cozinhaRepository.findAll();
	}

	//4.16. Customizando a representação em XML com Wrapper e anotações do Jackson - 5'
	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	public CozinhasXmlWrapper listarXml(){
		return new CozinhasXmlWrapper(cozinhaRepository.findAll());
	}

	//8.5. Simplificando o código com o uso de @ResponseStatus em exceptions - 2'20"
	@GetMapping("/{cozinhaId}")
	public Cozinha buscar(@PathVariable Long cozinhaId) {
		return cadastroCozinha.buscarOuFalhar(cozinhaId);
	}
	
	/*
	//5.4. Refatorando o código do projeto para usar o repositório do SDJ
	//4.20. Manipulando a resposta HTTP com ResponseEntity - 1'
	@GetMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId) {
		Optional<Cozinha> cozinha =  cozinhaRepository.findById(cozinhaId);
		
		if (cozinha.isPresent()) {
			return ResponseEntity.ok(cozinha.get());
		}
		
//		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		//ou
		return ResponseEntity.notFound().build();
	}
	*/
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cozinha adicionar(@RequestBody Cozinha cozinha) {
		Cozinha cozinhaSalva = cadastroCozinha.salvar(cozinha);
		return cozinhaSalva;
	}
	
	//8.5. Simplificando o código com o uso de @ResponseStatus em exceptions - 9'20"
	@PutMapping("/{cozinhaId}")
	public Cozinha atualizar(@PathVariable Long cozinhaId, @RequestBody Cozinha cozinha) {
		Cozinha cozinhaAtual = cadastroCozinha.buscarOuFalhar(cozinhaId);
		BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");//4.25. Modelando e implementando a atualização de recursos com PUT - 9' - O parâmetro "id" será ignorado no copyProperties 
		return cadastroCozinha.salvar(cozinhaAtual);
	}
	
	/*
	@PutMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> atualizar(@PathVariable Long cozinhaId, @RequestBody Cozinha cozinha) {
		Optional<Cozinha> cozinhaAtual = cozinhaRepository.findById(cozinhaId);
		
		if (cozinhaAtual.isPresent()) {
//			cozinhaAtual.setNome(cozinha.getNome());
			BeanUtils.copyProperties(cozinha, cozinhaAtual.get(), "id");//4.25. Modelando e implementando a atualização de recursos com PUT - 9' - O parâmetro "id" será ignorado no copyProperties 
			
			Cozinha cozinhaSalva = cadastroCozinha.salvar(cozinhaAtual.get());
			return ResponseEntity.ok(cozinhaSalva);
		}
		
		return ResponseEntity.notFound().build();
	}
	*/
	
	//8.4. Estendendo ResponseStatusException - 6'20"
	@DeleteMapping("/{cozinhaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cozinhaId) {	
		cadastroCozinha.excluir(cozinhaId);
	}
	
	/*
	//4.26. Modelando e implementando a exclusão de recursos com DELETE
	@DeleteMapping("/{cozinhaId}")
	public ResponseEntity<?> remover(@PathVariable Long cozinhaId) {
		try {
			cadastroCozinha.excluir(cozinhaId);
			return ResponseEntity.noContent().build();
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não encontrado");
		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}
	*/
	/*
	//8.2. Lançando exceções customizadas anotadas com @ResponseStatus - 7', 7'40"
	@DeleteMapping("/{cozinhaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cozinhaId) {	
		try {
			cadastroCozinha.excluir(cozinhaId);						
		} catch (EntidadeNaoEncontradaException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()); //Exception  base introduzida a partir do Spring 5
			//throw new ServerWebInputException(e.getMessage()); //8.3. Lançando exceções do tipo ResponseStatusException - 5'
		}
	}
	*/
}

/* Requests
Post com json - Content-Type - application/json
{    
    "nome": "Americana"
}

Post com xml - Content-Type - application/xml
<cozinha>    
	<nome>Japonesa</nome>
</cozinha>
*/
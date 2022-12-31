package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.CozinhaInputDisassembler;
import com.algaworks.algafood.api.assembler.CozinhaModelAssembler;
import com.algaworks.algafood.api.model.CozinhaModel;
import com.algaworks.algafood.api.model.CozinhasXmlWrapper;
import com.algaworks.algafood.api.model.input.CozinhaInput;
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
	
	@Autowired
	private CozinhaModelAssembler cozinhaModelAssembler;

	@Autowired
	private CozinhaInputDisassembler cozinhaInputDisassembler;
	
	//4.13. Implementando content negotiation para retornar JSON ou XML - 6'
	//@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE) //pode ser colocado no escopo da classe '@RequestMapping'	
	public List<CozinhaModel> listar() {
	    List<Cozinha> todasCozinhas = cozinhaRepository.findAll();
	    
	    return cozinhaModelAssembler.toCollectionModel(todasCozinhas);
	}

	//4.16. Customizando a representação em XML com Wrapper e anotações do Jackson - 5'
	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	public CozinhasXmlWrapper listarXml(){
		return new CozinhasXmlWrapper(cozinhaRepository.findAll());
	}

	//8.5. Simplificando o código com o uso de @ResponseStatus em exceptions - 2'20"
	@GetMapping("/{cozinhaId}")
	public CozinhaModel buscar(@PathVariable Long cozinhaId) {
	    Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);
	    
	    return cozinhaModelAssembler.toModel(cozinha);
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
	
	//https://app.algaworks.com/forum/topicos/80843/propriedade-consumes-da-annotation-postmapping
	//https://app.algaworks.com/forum/topicos/82285/nao-entendi-o-que-faz-a-classe-ser-capaz-de-responder-xml-mesmo-sem-eu-definir-ou-import-sobre-xml
	//@PostMapping(produces = MediaType.APPLICATION_XML_VALUE) //nesse caso, o método irá devolver em XML, se não especificar nada, irá devolver o que foi colocado no Accept o header da requisição
	//@PostMapping(consumes = MediaType.APPLICATION_XML_VALUE) //nesse caso, o método estará configurado para receber conteúdo apenas em XML, se mandar qualquer outro formato, será lançado o status 415 "Unsupported Media Type" - HttpMediaTypeNotSupportedException
	/*
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cozinha adicionar(@RequestBody @Valid Cozinha cozinha) {
		Cozinha cozinhaSalva = cadastroCozinha.salvar(cozinha);
		return cozinhaSalva;
	}
	*/
	
	//11.20. Desafio: usando DTOs como representation model
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CozinhaModel adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) {
	    Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput);
	    cozinha = cadastroCozinha.salvar(cozinha);
	    
	    return cozinhaModelAssembler.toModel(cozinha);
	}
	
	//11.20. Desafio: usando DTOs como representation model
	@PutMapping("/{cozinhaId}")
	public CozinhaModel atualizar(@PathVariable Long cozinhaId,
	        @RequestBody @Valid CozinhaInput cozinhaInput) {
	    Cozinha cozinhaAtual = cadastroCozinha.buscarOuFalhar(cozinhaId);
	    cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);
	    cozinhaAtual = cadastroCozinha.salvar(cozinhaAtual);
	    
	    return cozinhaModelAssembler.toModel(cozinhaAtual);
	}
	
	//8.5. Simplificando o código com o uso de @ResponseStatus em exceptions - 9'20"
	/*
	@PutMapping("/{cozinhaId}")
	public Cozinha atualizar(@PathVariable Long cozinhaId, @RequestBody @Valid Cozinha cozinha) {
		Cozinha cozinhaAtual = cadastroCozinha.buscarOuFalhar(cozinhaId);
		BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");//4.25. Modelando e implementando a atualização de recursos com PUT - 9' - O parâmetro "id" será ignorado no copyProperties 
		return cadastroCozinha.salvar(cozinhaAtual);
	}
	*/
	
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

//4.24. Negociando o media type do payload do POST com Content-Type - 1'10"
//Content-Type é o cabeçalho http colocado no header indicando qual o formato da representação que a requisição está enviando para o servidor
//Accept é o cabeçalho http colocado no header para indicar ao servidor o que aceita como resposta

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
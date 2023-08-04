package com.algaworks.algafood.api.v1.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
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

import com.algaworks.algafood.api.ResourceUriHelper;
import com.algaworks.algafood.api.v1.assembler.CidadeInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.CidadeModelAssembler;
import com.algaworks.algafood.api.v1.model.CidadeModel;
import com.algaworks.algafood.api.v1.model.input.CidadeInput;
import com.algaworks.algafood.core.web.AlgaMediaTypes_NAO_USADO;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

//20.10. Preparando o projeto para versionamento da API por Media Type - 6'30", 9'30", 12'50"

@RestController
@RequestMapping(path = "/v1/cidades", produces = MediaType.APPLICATION_JSON_VALUE) //20.13. Implementando o versionamento da API por URI - 1' 
public class CidadeController {

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CadastroCidadeService cadastroCidade;
	
	@Autowired
	private CidadeModelAssembler cidadeModelAssembler;

	@Autowired
	private CidadeInputDisassembler cidadeInputDisassembler;
	
	//19.11. Montando modelo de representação com RepresentationModelAssembler - 7'30"
	@Deprecated //20.18. Depreciando uma versão da API - 4'
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public CollectionModel<CidadeModel> listar() {
		List<Cidade> todasCidades = cidadeRepository.findAll();
		
		return cidadeModelAssembler.toCollectionModel(todasCidades);
	}
	
	/*
	//19.10. Adicionando hypermedia na representação de recursos de coleção - 1', 7'
//	@Override
	@GetMapping
	public CollectionModel<CidadeModel> listar() {
		List<Cidade> todasCidades = cidadeRepository.findAll();
		
		List<CidadeModel> cidadesModel = cidadeModelAssembler.toCollectionModel(todasCidades);
		
		cidadesModel.forEach(cidadeModel -> {
			cidadeModel.add(linkTo(methodOn(CidadeController.class)
					.buscar(cidadeModel.getId())).withSelfRel());
			
			cidadeModel.add(linkTo(methodOn(CidadeController.class)
					.listar()).withRel("cidades"));
			
			cidadeModel.getEstado().add(linkTo(methodOn(EstadoController.class)
					.buscar(cidadeModel.getEstado().getId())).withSelfRel());
		});
		
		CollectionModel<CidadeModel> cidadesCollectionModel = CollectionModel.of(cidadesModel); //Ver conteúdo de apoio - Após a versão 1.1 do Spring HATEOAS, o construtor da classe CollectionModel foi depreciado, no seu lugar usaremos o método estático CollectionModel.of
		
		cidadesCollectionModel.add(linkTo(CidadeController.class).withSelfRel());
		
		return cidadesCollectionModel;
	}
	*/
	
	/*
	@GetMapping
	public List<CidadeModel> listar() {
	    List<Cidade> todasCidades = cidadeRepository.findAll();
	    
	    return cidadeModelAssembler.toCollectionModel(todasCidades);
	}
	*/
	
	@GetMapping(path = "/{cidadeId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public CidadeModel buscar(@PathVariable Long cidadeId) {
		Cidade cidade = cadastroCidade.buscarOuFalhar(cidadeId);
		
		return cidadeModelAssembler.toModel(cidade);
	}
	
	/*
	//19.7. Adicionando hypermedia na representação de recurso único com HAL - 1'50", 10'
	//8.6. Desafio: refatorando os serviços REST
	@GetMapping("/{cidadeId}")
	public CidadeModel buscar(@PathVariable Long cidadeId) {
	    Cidade cidade = cadastroCidade.buscarOuFalhar(cidadeId);
	    
	    CidadeModel cidadeModel = cidadeModelAssembler.toModel(cidade);
	    
	    //19.9. Construindo links que apontam para métodos - 1'
		cidadeModel.add(linkTo(methodOn(CidadeController.class)
				.buscar(cidadeModel.getId())).withSelfRel());
	    
	    //19.8. Construindo links dinâmicos com WebMvcLinkBuilder - 1'50"
//	    cidadeModel.add(linkTo(CidadeController.class).slash(cidadeModel.getId()).withSelfRel());
//	    cidadeModel.add(Link.of("http://localhost:8080/cidades/1"));
	    
		
	    //19.9. Construindo links que apontam para métodos - 6'20"
		cidadeModel.add(linkTo(methodOn(CidadeController.class)
				.listar()).withRel("cidades"));
//		cidadeModel.add(linkTo(CidadeController.class).withRel("cidades"));
//		cidadeModel.add(Link.of("http://localhost:8080/cidades", "cidades"));
		
//		cidadeModel.add(Link.of("http://localhost:8080/cidades/1", IanaLinkRelations.SELF));

//		cidadeModel.add(Link.of("http://localhost:8080/cidades", IanaLinkRelations.COLLECTION));

		//19.9. Construindo links que apontam para métodos - 7'20"
		cidadeModel.getEstado().add(linkTo(methodOn(EstadoController.class)
				.buscar(cidadeModel.getEstado().getId())).withSelfRel());
//		cidadeModel.getEstado().add(linkTo(EstadoController.class).slash(cidadeModel.getEstado().getId()).withSelfRel());
//		cidadeModel.getEstado().add(Link.of("http://localhost:8080/estados/1"));

	    return cidadeModel;
	}
	*/
	
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
	
	
	//19.2. Adicionando a URI do recurso criado no header da resposta - 4'30", 14'
	//@Override
	@PostMapping(produces = AlgaMediaTypes_NAO_USADO.V1_APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeModel adicionar(@RequestBody @Valid CidadeInput cidadeInput) {
		try {
			Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);
			
			cidade = cadastroCidade.salvar(cidade);
			
			CidadeModel cidadeModel = cidadeModelAssembler.toModel(cidade);
			
			ResourceUriHelper.addUriInResponseHeader(cidadeModel.getId());
			
			return cidadeModel;
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	/*
	//11.20. Desafio: usando DTOs como representation model
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeModel adicionar(@RequestBody @Valid CidadeInput cidadeInput) {
	    try {
	        Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);
	        
	        cidade = cadastroCidade.salvar(cidade);
	        
	        return cidadeModelAssembler.toModel(cidade);
	    } catch (EstadoNaoEncontradoException e) {
	        throw new NegocioException(e.getMessage(), e);
	    }
	}
	*/
	
	//8.6. Desafio: refatorando os serviços REST
	/*
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
	public Cidade adicionar(@RequestBody @Valid Cidade cidade) { //9.9. Desafio: adicionando constraints de validação no modelo - @Valid ver anotações na classe RestauranteController.java
    	try {
    		Cidade cidadeSalva = cadastroCidade.salvar(cidade);
    		return cidadeSalva;
		} catch (EstadoNaoEncontradoException e) { //8.8. Criando a exception NegocioException - 10'
			throw new NegocioException(e.getMessage(), e);//8.10. Afinando a granularidade e definindo a hierarquia das exceptions de negócios - 16', 17'10"
		}
    }
	*/
	
	
//	@PostMapping
//	@ResponseStatus(HttpStatus.CREATED)
//	public Cidade adicionar(@RequestBody Cidade cidade) {		
//		cidade = cadastroCidade.salvar(cidade);
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
			cidade = cadastroCidade.salvar(cidade);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(cidade);
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
    */
	
    
    
    
	@PutMapping(path = "/{cidadeId}", produces = AlgaMediaTypes_NAO_USADO.V1_APPLICATION_JSON_VALUE)
	public CidadeModel atualizar(@PathVariable Long cidadeId,
	        @RequestBody @Valid CidadeInput cidadeInput) {
	    try {
	        Cidade cidadeAtual = cadastroCidade.buscarOuFalhar(cidadeId);
	        
	        cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);
	        
	        cidadeAtual = cadastroCidade.salvar(cidadeAtual);
	        
	        return cidadeModelAssembler.toModel(cidadeAtual);
	    } catch (EstadoNaoEncontradoException e) {
	        throw new NegocioException(e.getMessage(), e);
	    }
	}
    
    
    //8.6. Desafio: refatorando os serviços REST
	/*
    @PutMapping("/{cidadeId}")
    public Cidade atualizar(@PathVariable Long cidadeId, @RequestBody @Valid Cidade cidade) {
    	
    	try {
    		Cidade cidadeAtual = cadastroCidade.buscarOuFalhar(cidadeId);
    		BeanUtils.copyProperties(cidade, cidadeAtual, "id");//4.25. Modelando e implementando a atualização de recursos com PUT - 9' - O parâmetro "id" será ignorado no copyProperties
    		
    		return cadastroCidade.salvar(cidadeAtual);
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
    }
    */
        
    /*
	@PutMapping("/{cidadeId}")
	public ResponseEntity<Cidade> atualizar(@PathVariable Long cidadeId, @RequestBody Cidade cidade) {		
		Optional<Cidade> cidadeAtual = cidadeRepository.findById(cidadeId);
		
		if (cidadeAtual.isPresent()) {
			BeanUtils.copyProperties(cidade, cidadeAtual.get(), "id");//4.25. Modelando e implementando a atualização de recursos com PUT - 9' - O parâmetro "id" será ignorado no copyProperties
			Cidade cidadeSalva = cadastroCidade.salvar(cidadeAtual.get());
			return ResponseEntity.ok(cidadeSalva);
		}
		return ResponseEntity.notFound().build();
	}
	*/
    
    
    
    
    
    
    //8.6. Desafio: refatorando os serviços REST
    @DeleteMapping("/{cidadeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long cidadeId) {
    	cadastroCidade.excluir(cidadeId);
    }    
    
    /*
	@DeleteMapping("/{cidadeId}")
	public ResponseEntity<?> remover(@PathVariable Long cidadeId) {		
		try {
			cadastroCidade.excluir(cidadeId);
			return ResponseEntity.noContent().build();
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}
    */    
}

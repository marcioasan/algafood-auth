package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.RestauranteApenasNomeModelAssembler;
import com.algaworks.algafood.api.assembler.RestauranteBasicoModelAssembler;
import com.algaworks.algafood.api.assembler.RestauranteInputDisassembler;
import com.algaworks.algafood.api.assembler.RestauranteModelAssembler;
import com.algaworks.algafood.api.model.RestauranteApenasNomeModel;
import com.algaworks.algafood.api.model.RestauranteBasicoModel;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.api.model.RestauranteXmlWrapper;
import com.algaworks.algafood.api.model.input.CozinhaIdInput;
import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.api.model.view.RestauranteView;
import com.algaworks.algafood.core.validation.ValidacaoException;
import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

//@CrossOrigin(maxAge = 10) //16.4. Habilitando CORS em controladores e métodos com @CrossOrigin - 1' - Retirado na aula 16.6 e configurado no WebConfig.java
@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CadastroRestauranteService cadastroRestaurante;
	
	@Autowired
	private SmartValidator validator; //9.19. Executando processo de validação programaticamente - 4'40"
	
	@Autowired
	private RestauranteModelAssembler restauranteModelAssembler;
	
	@Autowired
	private RestauranteInputDisassembler restauranteInputDisassembler;
	
	@Autowired
	private RestauranteBasicoModelAssembler restauranteBasicoModelAssembler; //19.24. Desafio: adicionando hypermedia nos recursos de restaurantes

	@Autowired
	private RestauranteApenasNomeModelAssembler restauranteApenasNomeModelAssembler; 

//	@Override
//	@JsonView(RestauranteView.Resumo.class)
    @GetMapping
    public CollectionModel<RestauranteBasicoModel> listar() {
        return restauranteBasicoModelAssembler
                .toCollectionModel(restauranteRepository.findAll());
    }
    
//    @Override
//	@JsonView(RestauranteView.ApenasNome.class)
    @GetMapping(params = "projecao=apenas-nome")
    public CollectionModel<RestauranteApenasNomeModel> listarApenasNomes() {
        return restauranteApenasNomeModelAssembler
                .toCollectionModel(restauranteRepository.findAll());
    }
	
	/*
	@JsonView(RestauranteView.Resumo.class)
	@GetMapping
	public List<RestauranteModel> listar() {
		return restauranteModelAssembler.toCollectionModel(restauranteRepository.findAll());
	}
	
	@JsonView(RestauranteView.ApenasNome.class)
	@GetMapping(params = "projecao=apenas-nome")
	public List<RestauranteModel> listarApenasNomes() {
		return listar();
	}
	*/
	
	//13.1. Fazendo projeção de recursos com @JsonView do Jackson - 9'50", 13'20"(@RequestParam)
	/*
	//@GetMapping
	public MappingJacksonValue listar(@RequestParam(required = false) String projecao) { //MappingJacksonValue é um wrapper (envelopador) que vai envelopar o retorno da lista de RestauranteModel.
		List<Restaurante> restaurantes = restauranteRepository.findAll();
		List<RestauranteModel> restaurantesModel = restauranteModelAssembler.toCollectionModel(restaurantes);
		
		MappingJacksonValue restaurantesWrapper = new MappingJacksonValue(restaurantesModel);
		
		restaurantesWrapper.setSerializationView(RestauranteView.Resumo.class);
		
		if ("apenas-nome".equals(projecao)) {
			restaurantesWrapper.setSerializationView(RestauranteView.ApenasNome.class);
		} else if ("completo".equals(projecao)) {
			restaurantesWrapper.setSerializationView(null);
		}
		
		return restaurantesWrapper;
	}
	*/
    
	//INÍCIO AULA - 16.3. Entendendo o funcionamento básico de CORS e habilitando na API - 2'30"
	/*
	@JsonView(RestauranteView.Resumo.class)
	@GetMapping
	public ResponseEntity<List<RestauranteModel>> listar() {
		List<RestauranteModel> restaurantesModel = restauranteModelAssembler
				.toCollectionModel(restauranteRepository.findAll());
		
		return ResponseEntity.ok()
				.header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
				.body(restaurantesModel);
	}
	*/
	//FIM AULA - 16.3. Entendendo o funcionamento básico de CORS e habilitando na API
	
	/*
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<RestauranteModel> listar(){
		return restauranteModelAssembler.toCollectionModel(restauranteRepository.findAll());
	}
	
	//13.1. Fazendo projeção de recursos com @JsonView do Jackson - 3'40", 5'30", 6'50"
	@JsonView(RestauranteView.Resumo.class)
	@GetMapping(params = "projecao=resumo")
	public List<RestauranteModel> listarResumido(){
		return listar();
	}

	//13.1. Fazendo projeção de recursos com @JsonView do Jackson - 3'40", 5'30", 6'50", 8'
	@JsonView(RestauranteView.ApenasNome.class)
	@GetMapping(params = "projecao=apenas-nome")
	public List<RestauranteModel> listarApenasNomes(){
		return listar();
	}
	*/
	
	//4.16. Customizando a representação em XML com Wrapper e anotações do Jackson - 5'
	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	public RestauranteXmlWrapper listarXml(){
		return new RestauranteXmlWrapper(restauranteRepository.findAll());
	}
	
	
	
	
	
	
	//8.6. Desafio: refatorando os serviços REST
	@GetMapping("/{idRestaurante}")
	public RestauranteModel buscar(@PathVariable Long idRestaurante) {
		Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(idRestaurante);
		
		return restauranteModelAssembler.toModel(restaurante);
	}
	
	/*
	@GetMapping("/{idRestaurante}")
	public ResponseEntity<Restaurante> buscar(@PathVariable Long idRestaurante) {
		Optional<Restaurante> restaurante = restauranteRepository.findById(idRestaurante);
		if (restaurante.isPresent()) {
			return ResponseEntity.ok(restaurante.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	*/
	
	
	
	
	
	//8.6. Desafio: refatorando os serviços REST
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	//public Restaurante adicionar(@RequestBody @Valid Restaurante restaurante) {  //9.2. Adicionando constraints e validando no controller com @Valid - 6'30"
	//public Restaurante adicionar(@RequestBody @Validated(Groups.CadastroRestaurante.class) Restaurante restaurante) {  //9.7. Agrupando e restringindo constraints que devem ser usadas na validação - 10'
	public RestauranteModel adicionar(@RequestBody @Valid RestauranteInput restauranteInput) {	//9.8. Convertendo grupos de constraints para validação em cascata com @ConvertGroup - 2'40"
		try {
			Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput); //11.11. Criando DTOs para entrada de dados na API - 13'
			return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restaurante)); //11.10. Implementando a conversão de entidade para DTO - 7'10"
		} catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException  e) { //12.7. Refatorando serviço de cadastro de restaurante para incluir endereço - 16'
			throw new NegocioException(e.getMessage());
		}
	}
	
	/*
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante){//4.30. Modelando e implementando a inclusão de recursos de restaurantes - 21' ResponseEntity<?> 
		try {
			restaurante = cadastroRestaurante.salvar(restaurante);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(restaurante);
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	*/
	
	//11.17. Mapeando para uma instância destino (e não um tipo) com ModelMapper
	@PutMapping("/{restauranteId}")
	public RestauranteModel atualizar(@PathVariable Long restauranteId,
			@RequestBody @Valid RestauranteInput restauranteInput) {
		try {
			Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);
			
			restauranteInputDisassembler.copyToDomainObject(restauranteInput, restauranteAtual);
			
			return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restauranteAtual));
		} catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) { //12.7. Refatorando serviço de cadastro de restaurante para incluir endereço - 16'
			throw new NegocioException(e.getMessage());
		}
	}
	
	
	
	
	//8.6. Desafio: refatorando os serviços REST
	/*
	@PutMapping("/{restauranteId}")
	public RestauranteModel atualizar(@PathVariable Long restauranteId, @RequestBody @Valid RestauranteInput restauranteInput) {
		Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput); ////11.11. Criando DTOs para entrada de dados na API - 13'
		Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);
	    BeanUtils.copyProperties(restaurante, restauranteAtual, "id", "formasPagamento", "endereco", "dataCadastro", "produtos");//4.25. Modelando e implementando a atualização de recursos com PUT - 9' - O parâmetro "id" será ignorado no copyProperties, 6.3. Analisando o impacto do relacionamento muitos-para-muitos na REST API - 7'30", incluir no copyProperties "formasPagamento" e "endereco" para ignorar essas propriedades que estão vindo vazias do request, 6.5 - 7'

	    try {
	    	return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restauranteAtual));
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}
	*/
	
	/*
	@PutMapping("/{restauranteId}")	
	public ResponseEntity<?> atualizar(@PathVariable Long restauranteId, @RequestBody Restaurante restaurante) {
		try {
			Optional<Restaurante> restauranteAtual = restauranteRepository.findById(restauranteId);
			
			if (restauranteAtual.isPresent()) {
				BeanUtils.copyProperties(restaurante, restauranteAtual.get(), "id", "formasPagamento", "endereco", "dataCadastro", "produtos");//4.25. Modelando e implementando a atualização de recursos com PUT - 9' - O parâmetro "id" será ignorado no copyProperties, 6.3. Analisando o impacto do relacionamento muitos-para-muitos na REST API - 7'30", incluir no copyProperties "formasPagamento" e "endereco" para ignorar essas propriedades que estão vindo vazias do request, 6.5 - 7' 
				Restaurante restauranteSalvo = cadastroRestaurante.salvar(restauranteAtual.get());
				return ResponseEntity.ok(restauranteSalvo);				
			}
			return ResponseEntity.notFound().build();
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	*/
	
	
	
	
	
	
	
	
	
	
	//8.6. Desafio: refatorando os serviços REST
	@PatchMapping("/{restauranteId}")
	public RestauranteModel atualizarParcial(@PathVariable Long restauranteId, @RequestBody Map<String, Object> campos, HttpServletRequest request) { //8.24. Lançando exception de desserialização na atualização parcial (PATCH) - 10'20"
	    Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);
	    merge(campos, restauranteAtual, request);
	    
	    //9.19. Executando processo de validação programaticamente - 2'50"
	    validate(restauranteAtual, "restaurante");
	    
	    //Esse método atualizarParcial foi retirado do curso, mas eu mantive e devido a criação do RestauranteInput, precisei criar o toRestauranteInput para o método funcionar.
	    toRestauranteInput(restauranteAtual);
	    return atualizar(restauranteId, toRestauranteInput(restauranteAtual));
	    //return atualizar(restauranteId, restauranteAtual);
	}
	
	private void validate(Restaurante restaurante, String objectName) {
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restaurante, objectName);
		validator.validate(restaurante, bindingResult);
		
		if (bindingResult.hasErrors()) {
			throw new ValidacaoException(bindingResult);
		}
	}

	/*
	//4.33. Analisando solução para atualização parcial de recursos com PATCH
	@PatchMapping("/{restauranteId}")
	public ResponseEntity<?> atualizarParcial(@PathVariable Long restauranteId, @RequestBody Map<String, Object> campos) {
		
		Optional<Restaurante> restauranteAtual = restauranteRepository.findById(restauranteId);
		
		merge(campos, restauranteAtual.get());
		
		return atualizar(restauranteId, restauranteAtual.get());
	}
	*/
	
	
	//4.34. Finalizando a atualização parcial com a API de Reflections do Spring - 2'30", 5'50", 8'50" 
	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino, HttpServletRequest request) { //8.24. Lançando exception de desserialização na atualização parcial (PATCH) - 10'20"

		ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
		
		//8.24. Lançando exception de desserialização na atualização parcial (PATCH) - 6'30"
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true); //8.24. Lançando exception de desserialização na atualização parcial (PATCH) - 1'30", 4'30"
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
			
			Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);
			
			System.out.println("********** " + restauranteOrigem);
			
			
			
			dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
				Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
				field.setAccessible(true);
				
				Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
				
				System.out.println("---> " + nomePropriedade + " = " + valorPropriedade + " = " + novoValor);
				
				ReflectionUtils.setField(field, restauranteDestino, novoValor);
			});			
		} catch (IllegalArgumentException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
		}
	}
	
	private RestauranteInput toRestauranteInput(Restaurante restaurante) {
		RestauranteInput restauranteInput = new RestauranteInput();
		restauranteInput.setNome(restaurante.getNome());
		restauranteInput.setTaxaFrete(restaurante.getTaxaFrete());
		restauranteInput.setCozinha(toCozinhaInput(restaurante.getCozinha()));
		return restauranteInput;
	}
	
	private CozinhaIdInput toCozinhaInput(Cozinha cozinha) {
		CozinhaIdInput cozinhaIdInput =  new CozinhaIdInput();
		cozinhaIdInput.setId(cozinha.getId());
		return cozinhaIdInput;
	}

//	@Override
	@PutMapping("/{restauranteId}/abertura")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> abrir(@PathVariable Long restauranteId) {
	    cadastroRestaurante.abrir(restauranteId);
	    
	    return ResponseEntity.noContent().build();
	}

//	@Override
	@PutMapping("/{restauranteId}/fechamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> fechar(@PathVariable Long restauranteId) {
	    cadastroRestaurante.fechar(restauranteId);
	    
	    return ResponseEntity.noContent().build();
	}
	
	/*
	//12.14. Desafio: Implementando os endpoints de abertura e fechamento de restaurantes
	@PutMapping("/{restauranteId}/abertura")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void abrir(@PathVariable Long restauranteId) {
	    cadastroRestaurante.abrir(restauranteId);
	}

	@PutMapping("/{restauranteId}/fechamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void fechar(@PathVariable Long restauranteId) {
	    cadastroRestaurante.fechar(restauranteId);
	} 
	*/
	
	//19.24. Desafio: adicionando hypermedia nos recursos de restaurantes
//	@Override
	@PutMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> ativar(@PathVariable Long restauranteId) {
	    cadastroRestaurante.ativar(restauranteId);
	    
	    return ResponseEntity.noContent().build();
	}

//	@Override
	@DeleteMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> inativar(@PathVariable Long restauranteId) {
	    cadastroRestaurante.inativar(restauranteId);
	    
	    return ResponseEntity.noContent().build();
	}
	
	/*
	//12.4. Implementando os endpoints de ativação e inativação de restaurantes - 12'30"
	@PutMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativar(@PathVariable Long restauranteId) {
		cadastroRestaurante.ativar(restauranteId);
	}
	
	@DeleteMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativar(@PathVariable Long restauranteId) {
		cadastroRestaurante.inativar(restauranteId);
	}
	*/
	
	//12.18. Implementando ativação e inativação em massa de restaurantes
	@PutMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativarMultiplos(@RequestBody List<Long> restauranteIds) {
		try {
			cadastroRestaurante.ativar(restauranteIds);
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@DeleteMapping("/desativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativarMultiplos(@RequestBody List<Long> restauranteIds) {
		try {
			cadastroRestaurante.inativar(restauranteIds);
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
}

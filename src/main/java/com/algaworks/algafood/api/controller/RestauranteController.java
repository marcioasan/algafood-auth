package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.Groups;
import com.algaworks.algafood.api.model.RestauranteXmlWrapper;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CadastroRestauranteService cadastroRestaurante;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Restaurante> listar(){
		return restauranteRepository.findAll();
	}
	
	//4.16. Customizando a representação em XML com Wrapper e anotações do Jackson - 5'
	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	public RestauranteXmlWrapper listarXml(){
		return new RestauranteXmlWrapper(restauranteRepository.findAll());
	}
	
	
	
	
	
	
	//8.6. Desafio: refatorando os serviços REST
	@GetMapping("/{idRestaurante}")
	public Restaurante buscar(@PathVariable Long idRestaurante) {
		Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(idRestaurante);
		return restauranteAtual;
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
	public Restaurante adicionar(@RequestBody @Valid Restaurante restaurante) {	//9.8. Convertendo grupos de constraints para validação em cascata com @ConvertGroup - 2'40"
		try {
			return cadastroRestaurante.salvar(restaurante);			
		} catch (CozinhaNaoEncontradaException e) {
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
	
	
	
	
	
	
	//8.6. Desafio: refatorando os serviços REST
	@PutMapping("/{restauranteId}")
	public Restaurante atualizar(@PathVariable Long restauranteId, @RequestBody Restaurante restaurante) {
	    Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);
	    BeanUtils.copyProperties(restaurante, restauranteAtual, "id", "formasPagamento", "endereco", "dataCadastro", "produtos");//4.25. Modelando e implementando a atualização de recursos com PUT - 9' - O parâmetro "id" será ignorado no copyProperties, 6.3. Analisando o impacto do relacionamento muitos-para-muitos na REST API - 7'30", incluir no copyProperties "formasPagamento" e "endereco" para ignorar essas propriedades que estão vindo vazias do request, 6.5 - 7'

	    try {
	    	return cadastroRestaurante.salvar(restauranteAtual);			
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}
	
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
	public Restaurante atualizarParcial(@PathVariable Long restauranteId, @RequestBody Map<String, Object> campos, HttpServletRequest request) { //8.24. Lançando exception de desserialização na atualização parcial (PATCH) - 10'20"
	    Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);
	    merge(campos, restauranteAtual, request);
	    return atualizar(restauranteId, restauranteAtual);
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

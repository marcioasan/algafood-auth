package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Restaurante> listar(){
		return restauranteRepository.listar();
	}
	
	@GetMapping("/{idRestaurante}")
	public ResponseEntity<Restaurante> buscar(@PathVariable Long idRestaurante) {
		Restaurante restaurante = restauranteRepository.buscar(idRestaurante);
		if (restaurante != null) {
			return ResponseEntity.ok(restaurante);
		}
		
		return ResponseEntity.notFound().build();
	}
}

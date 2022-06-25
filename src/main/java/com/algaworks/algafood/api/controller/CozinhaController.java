package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

@RestController
@RequestMapping("/cozinhas") //quando passa só um parâmetro, não precisa da palavra 'value'
//@RequestMapping(value = "/cozinhas", produces = MediaType.APPLICATION_JSON_VALUE)
public class CozinhaController {

	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	//4.13. Implementando content negotiation para retornar JSON ou XML - 6'
	//@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE,  MediaType.APPLICATION_XML_VALUE})
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE) //pode ser colocado no escopo da classe '@RequestMapping'	
	public List<Cozinha> listar() {
		System.out.println("LISTAR 1");
		return cozinhaRepository.listar();
	}
	
	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	public List<Cozinha> listar2() {
		System.out.println("LISTAR 2");
		return cozinhaRepository.listar();
	}
	
}
package com.algaworks.algafood.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//8.5. Simplificando o código com o uso de @ResponseStatus em exceptions
@ResponseStatus(value = HttpStatus.NOT_FOUND) //8.2. Lançando exceções customizadas anotadas com @ResponseStatus - 3', 4', 4'50"
public abstract class EntidadeNaoEncontradaException extends NegocioException { //8.11. Desafio: lançando exceptions de granularidade fina

	private static final long serialVersionUID = 1L;
	
	public EntidadeNaoEncontradaException(String mensagem) {
		super(mensagem);
	}
}


/*
//@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Entidade não encontrada")
//@ResponseStatus(value = HttpStatus.NOT_FOUND) //8.2. Lançando exceções customizadas anotadas com @ResponseStatus - 3', 4', 4'50"
public class EntidadeNaoEncontradaException extends ResponseStatusException { //8.4. Estendendo ResponseStatusException - 1'30"

	private static final long serialVersionUID = 1L;

	//8.4. Estendendo ResponseStatusException - 1'50", 3'20"
	public EntidadeNaoEncontradaException(HttpStatus status, String mensagem) {
		super(status, mensagem);		
	}

	public EntidadeNaoEncontradaException(String mensagem) {
		this(HttpStatus.NOT_FOUND, mensagem);
	}
	
}
*/
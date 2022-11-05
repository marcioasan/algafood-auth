package com.algaworks.algafood.domain.exception;

//@ResponseStatus(HttpStatus.CONFLICT) //8.2. Lançando exceções customizadas anotadas com @ResponseStatus - 8' //retirado na aula 8.14. Desafio: implementando exception handler
public class EntidadeEmUsoException extends NegocioException {

	private static final long serialVersionUID = 1L;

	public EntidadeEmUsoException(String mensagem) {
		super(mensagem);
	}
}

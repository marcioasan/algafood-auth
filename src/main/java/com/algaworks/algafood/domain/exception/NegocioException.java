package com.algaworks.algafood.domain.exception;

//8.8. Criando a exception NegocioException - 5'20"
//@ResponseStatus(value = HttpStatus.BAD_REQUEST) //retirado na aula 8.14. Desafio: implementando exception handler
public class NegocioException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NegocioException(String mensagem) {
		super(mensagem);
	}
	
	//8.10. Afinando a granularidade e definindo a hierarquia das exceptions de negócios - 16'
	public NegocioException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
}
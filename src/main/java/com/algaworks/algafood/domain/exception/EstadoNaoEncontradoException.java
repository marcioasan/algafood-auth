package com.algaworks.algafood.domain.exception;

//8.10. Afinando a granularidade e definindo a hierarquia das exceptions de negócios - 4'30", 6'
public class EstadoNaoEncontradoException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;
	
	public EstadoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	//8.10. Afinando a granularidade e definindo a hierarquia das exceptions de negócios - 12'10"
	public EstadoNaoEncontradoException(Long estadoId) {
		this(String.format("Não existe um cadastro de estado com código %d", estadoId));
	}
}
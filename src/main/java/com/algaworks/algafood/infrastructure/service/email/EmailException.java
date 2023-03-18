package com.algaworks.algafood.infrastructure.service.email;

//15.3. Implementando o serviço de infraestrutura de envio de e-mails com Spring - 

public class EmailException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmailException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmailException(String message) {
		super(message);
	}
}

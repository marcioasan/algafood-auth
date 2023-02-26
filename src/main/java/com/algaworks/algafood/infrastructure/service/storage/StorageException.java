package com.algaworks.algafood.infrastructure.service.storage;

//14.8. Implementando o serviço de armazenagem de fotos no disco local - 15'20"

public class StorageException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}

	public StorageException(String message) {
		super(message);
	}

}

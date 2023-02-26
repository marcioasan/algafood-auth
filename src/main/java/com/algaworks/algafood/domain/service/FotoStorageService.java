package com.algaworks.algafood.domain.service;

import java.io.InputStream;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

//14.8. Implementando o serviço de armazenagem de fotos no disco local - 1'50", 5'

public interface FotoStorageService {

	void armazenar(NovaFoto novaFoto);
	
	default String gerarNomeArquivo(String nomeOriginal) {
		return UUID.randomUUID().toString() + "_" + nomeOriginal;
	}
	
	@Builder
	@Getter
	class NovaFoto {
		
		private String nomeAquivo;
		private InputStream inputStream;
		
	}
}

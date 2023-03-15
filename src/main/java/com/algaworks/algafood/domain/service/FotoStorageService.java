package com.algaworks.algafood.domain.service;

import java.io.InputStream;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

//14.8. Implementando o serviço de armazenagem de fotos no disco local - 1'50", 5'
//14.10. Implementando a remoção e substituição de arquivos de fotos no serviço de armazenagem - 1'
public interface FotoStorageService {

	InputStream recuperar(String nomeArquivo); //14.11. Desafio: Implementando recuperação de foto no serviço de armazenagem
	
	void armazenar(NovaFoto novaFoto);
	
	void remover(String nomeArquivo);
	
	default void substituir(String nomeArquivoAntigo, NovaFoto novaFoto) {
		this.armazenar(novaFoto);
		
		if (nomeArquivoAntigo != null) {
			this.remover(nomeArquivoAntigo);
		}
	}
	
	default String gerarNomeArquivo(String nomeOriginal) {
		return UUID.randomUUID().toString() + "_" + nomeOriginal;
	}
	
	@Builder
	@Getter
	class NovaFoto {
		
		private String nomeAquivo;
		private String contentType; //14.23. Implementando a inclusão de objetos no bucket da Amazon S3 - 11'30"
		private InputStream inputStream;
		
	}
}

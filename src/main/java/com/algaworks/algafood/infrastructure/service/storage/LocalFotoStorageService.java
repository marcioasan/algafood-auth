package com.algaworks.algafood.infrastructure.service.storage;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.algaworks.algafood.core.storage.StorageProperties;
import com.algaworks.algafood.domain.service.FotoStorageService;


//14.8. Implementando o serviço de armazenagem de fotos no disco local - 7'30"
//14.26. Selecionando a implementação do serviço de storage de fotos - 1'

public class LocalFotoStorageService implements FotoStorageService {

	//***Retirado na aula 14.20. Criando bean de propriedades de configuração dos serviços de storage - 14'
	//@Value("${algafood.storage.local.diretorio-fotos}") //#14.8. Implementando o serviço de armazenagem de fotos no disco local - 13'
	//private Path diretorioFotos;
	
	@Autowired
	private StorageProperties storageProperties;
	
	@Override
	public void armazenar(NovaFoto novaFoto) {
		try {
			Path arquivoPath = getArquivoPath(novaFoto.getNomeAquivo());
			
			FileCopyUtils.copy(novaFoto.getInputStream(), 
					Files.newOutputStream(arquivoPath));
		} catch (Exception e) {
			throw new StorageException("Não foi possível armazenar arquivo.", e);
		}
	}
	
	//14.10. Implementando a remoção e substituição de arquivos de fotos no serviço de armazenagem - 1'40"
	@Override
	public void remover(String nomeArquivo) {
		try {
			Path arquivoPath = getArquivoPath(nomeArquivo);
			
			Files.deleteIfExists(arquivoPath);
		} catch (Exception e) {
			throw new StorageException("Não foi possível excluir arquivo.", e);
		}
	}
	
	private Path getArquivoPath(String nomeArquivo) {
//		return diretorioFotos.resolve(Path.of(nomeArquivo));
		return storageProperties.getLocal().getDiretorioFotos().resolve(Path.of(nomeArquivo)); //***Retirado na aula 14.20. Criando bean de propriedades de configuração dos serviços de storage - 14'
	}

	//14.25. Implementando a recuperação de foto no serviço de storage do S3 - 3'30"
	@Override
	public FotoRecuperada recuperar(String nomeArquivo) {
		try {
			Path arquivoPath = getArquivoPath(nomeArquivo);

			FotoRecuperada fotoRecuperada = FotoRecuperada.builder()
					.inputStream(Files.newInputStream(arquivoPath))
					.build();
			
			return fotoRecuperada;
		} catch (Exception e) {
			throw new StorageException("Não foi possível recuperar arquivo.", e);
		}
	}
	
	
	//14.11. Desafio: Implementando recuperação de foto no serviço de armazenagem
	/*@Override
	public InputStream recuperar(String nomeArquivo) {
	    try {
	        Path arquivoPath = getArquivoPath(nomeArquivo);

	        return Files.newInputStream(arquivoPath);
	    } catch (Exception e) {
	        throw new StorageException("Não foi possível recuperar arquivo.", e);
	    }
	}
	*/
}

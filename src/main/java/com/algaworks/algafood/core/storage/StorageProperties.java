package com.algaworks.algafood.core.storage;

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.amazonaws.regions.Regions;

import lombok.Getter;
import lombok.Setter;

//14.20. Criando bean de propriedades de configuração dos serviços de storage - 2' - 6'

@Getter
@Setter
@Component
@ConfigurationProperties("algafood.storage")
public class StorageProperties {

	private Local local = new Local();
	private S3 s3 = new S3();
	
	private TipoStorage tipo = TipoStorage.LOCAL; //14.26. Selecionando a implementação do serviço de storage de fotos - 2'50", 4'51 - configura se é local ou se é no S3 no application.properties na propriedade algafood.storage.tipo
	
	public enum TipoStorage {
		LOCAL, S3	
	}
	
	@Getter
	@Setter
	public class Local {
		
		private Path diretorioFotos;
		
	}
	
	@Getter
	@Setter
	public class S3 {
		
		private String idChaveAcesso;
		private String chaveAcessoSecreta;
		private String bucket;
//		private String regiao;
		private Regions regiao; //14.22. Definindo bean do client da Amazon S3 e configurando credenciais - 5'30"
		private String diretorioFotos;
		
	}
}

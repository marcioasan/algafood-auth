package com.algaworks.algafood.core.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.mediatype.hal.HalConfiguration;
import org.springframework.http.MediaType;

//20.10. Preparando o projeto para versionamento da API por Media Type - VER CONTEÚDO DE APOIO - Na versão 2.5.6 do Spring Boot, não é mais necessário criar a classe HalCustomMediaTypeEnabler, no lugar dela vamos criar uma classe de configuração AlgaHalConfiguration, e informar quais os media types aceitos para o HAL:

@Configuration
public class AlgaHalConfiguration {

	@Bean
	public HalConfiguration globalPolicy() {
		return new HalConfiguration()
				.withMediaType(MediaType.APPLICATION_JSON)
				.withMediaType(AlgaMediaTypes.V1_APPLICATION_JSON)
				.withMediaType(AlgaMediaTypes.V2_APPLICATION_JSON); //20.11. Implementando o versionamento da API por Media Type - 16'30" - Está no conteúdo de apoio
	}
	
}

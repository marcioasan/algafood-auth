package com.algaworks.algafood.core.web;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//16.6. Habilitando CORS globalmente no projeto da API

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedMethods("*");
//			.allowedOrigins("*")
//			.maxAge(30);
	}
	
	//20.12. Definindo a versão padrão da API quando o Media Type não é informado - 2'30"
	//20.13. Implementando o versionamento da API por URI - retirado nessa aula 5'
//	@Override
//	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
//		configurer.defaultContentType(AlgaMediaTypes.V2_APPLICATION_JSON);
//	}
	
	//17.5. Implementando requisições condicionais com Shallow ETags - 7'40"
	@Bean
	public Filter shallowEtagHeaderFilter() {
		return new ShallowEtagHeaderFilter();
	}
	
}

package com.algaworks.algafood.core.squiggly;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.web.RequestSquigglyContextProvider;
import com.github.bohnman.squiggly.web.SquigglyRequestFilter;

//13.3. Limitando os campos retornados pela API com Squiggly - 1'30", 3'50"
@Configuration
public class SquigglyConfig {

	//Adiciona um filtro nas requisições HTTP, sempre que uma requisição chegar na API, vai passar por esse filtro - 3'50" | OBS: essa classe é chamada durante a inicialização do projeto, não é chamada quando executa a requisição
	@Bean
	public FilterRegistrationBean<SquigglyRequestFilter> squigglyRequestFilter(ObjectMapper objectMapper) {
		Squiggly.init(objectMapper, new RequestSquigglyContextProvider("campos", null));		
		
		var urlPatterns = Arrays.asList("/pedidos/*", "/restaurantes/*");
		
		var filterRegistration = new FilterRegistrationBean<SquigglyRequestFilter>();
		filterRegistration.setFilter(new SquigglyRequestFilter());
		filterRegistration.setOrder(1);
		filterRegistration.setUrlPatterns(urlPatterns);
		
		return filterRegistration;
	}
}

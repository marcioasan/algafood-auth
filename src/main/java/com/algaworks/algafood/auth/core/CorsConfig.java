package com.algaworks.algafood.auth.core;
import java.util.Collections;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

//22.19. Testando o fluxo Authorization Code com um client JavaScript - 9'30"

@Configuration
public class CorsConfig {

	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOriginPattern("*");
//		config.setAllowedOrigins(Collections.singletonList("*"));
		config.setAllowedMethods(Collections.singletonList("*"));
		config.setAllowedHeaders(Collections.singletonList("*"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/oauth/token", config);

		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		
		return bean;
	}

}
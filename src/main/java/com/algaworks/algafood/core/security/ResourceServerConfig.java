package com.algaworks.algafood.core.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//*22.11. Configurando o Resource Server com a nova stack do Spring Security - 2', 3'20"

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.anyRequest().authenticated()
			.and()
			.oauth2ResourceServer() //configura um Resource Server do OAuth2 no projeto algafood-api
			.opaqueToken(); //O Authorization Server emite tokens opacos, que não podem ser revertidos
	}
}

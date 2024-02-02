package com.algaworks.algafood.core.security;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

//*22.11. Configurando o Resource Server com a nova stack do Spring Security - 2', 3'20"

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

	//Modificado na aula 23.6. Configurando o Resource Server para JWT assinado com chave simétrica - 4'20"
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.anyRequest().authenticated()
			.and()
			.cors().and() //22.19. Testando o fluxo Authorization Code com um client JavaScript - 12'
			.oauth2ResourceServer() //configura um Resource Server do OAuth2 no projeto algafood-api
			.jwt(); //O Authorization Server emite tokens opacos, que não podem ser revertidos
	}
	
	/*** Removido na aula 23.12. Configurando a validação de JWT no Resource Server com a chave pública - 1'50"
	//Modificado na aula 23.6. Configurando o Resource Server para JWT assinado com chave simétrica - 4'30"
	@Bean
	public JwtDecoder jwtDecoder() {
		var secretKey = new SecretKeySpec("89a7sd89f7as98f7dsa98fds7fd89sasd9898asdf98s".getBytes(), "HmacSHA256");
		
		return NimbusJwtDecoder.withSecretKey(secretKey).build();
	}
	*/
	
	/* Modificado na aula 23.6. Configurando o Resource Server para JWT assinado com chave simétrica - 4'
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.anyRequest().authenticated()
			.and()
			.cors().and() //22.19. Testando o fluxo Authorization Code com um client JavaScript - 12'
			.oauth2ResourceServer() //configura um Resource Server do OAuth2 no projeto algafood-api
			.opaqueToken(); //O Authorization Server emite tokens opacos, que não podem ser revertidos
	}
	*/
}

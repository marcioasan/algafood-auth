package com.algaworks.algafood.auth.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//22.8. Criando o projeto do Authorization Server com Spring Security OAuth2 - 8', 11'

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	/* removido na aula 23.14. Autenticando usuario com dados do banco de dados - 14' pois passou a autenticar pelo BD
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser("marcio") //Credenciais do usuário (Resource Owner) 14'30" mostra a configuração no Postman
				.password(passwordEncoder().encode("123"))
				.roles("ADMIN")
			.and()
			.withUser("joao")
				.password(passwordEncoder().encode("123"))
				.roles("ADMIN");
	}
	*/
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//22.9. Configurando o fluxo Authorization Server com Password Credentials e Opaque Tokens - 11'20"
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	//22.13. Configurando o Refresh Token Grant Type no Authorization Server - 4'
	/* removido na aula 23.14. Autenticando usuario com dados do banco de dados - 14'30" 
	  pois passou a usar o JpaUserDetailsService que foi implementado e que já está exposto como um Bean do Spring	 
	@Bean
	@Override
	protected UserDetailsService userDetailsService() {
		return super.userDetailsService();
	}
	*/
	
}

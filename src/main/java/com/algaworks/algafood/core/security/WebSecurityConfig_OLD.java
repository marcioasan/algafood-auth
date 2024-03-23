package com.algaworks.algafood.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//*22.11. Configurando o Resource Server com a nova stack do Spring Security - 2' - Essa classe foi renomeada de WebSecurityConfig.java para ResourceServerConfig.java
//22.3. Configurando Spring Security com HTTP Basic - 40",, 4'30", 5', 9'(csrf())

//@Configuration
//@EnableWebSecurity
public class WebSecurityConfig_OLD extends WebSecurityConfigurerAdapter { //versões mais recentes do Spring Boot depreciaram essa extensão - https://app.algaworks.com/forum/topicos/86909/websecurityconfigureradapter-esta-depreciada

	//22.4. Configurando autenticação de usuários em memória
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser("marcio")
				.password(passwordEncoder().encode("123"))
				.roles("ADMIN")
			.and()
			.withUser("joao")
				.password(passwordEncoder().encode("123"))
				.roles("ADMIN");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic()
			
			.and()
			.authorizeRequests()
				.antMatchers("/v1/cozinhas/**").permitAll() //permite qualquer requisição com essa url
				.anyRequest().authenticated()
			
			.and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS) //configuração para não usar sessão, não cria os cookies com o session id
				
			.and()
				.csrf().disable(); //9'
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
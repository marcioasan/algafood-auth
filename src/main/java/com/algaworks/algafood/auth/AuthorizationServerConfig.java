package com.algaworks.algafood.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
//22.8. Criando o projeto do Authorization Server com Spring Security OAuth2 - 12'

@Configuration
@EnableAuthorizationServer //***Habilita o projeto para ser um Authorization Server
@SuppressWarnings("deprecation")
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager; //essa injeção não existe no contexto do Spring, precisa configurar no WebSecurityConfig - 11'20" 
	
	//22.13. Configurando o Refresh Token Grant Type no Authorization Server - 5' - essa injeção não existe no contexto do Spring, precisa configurar no WebSecurityConfig
	@Autowired
	UserDetailsService userDetailsService; 
	
	//22.9. Configurando o fluxo Authorization Server com Password Credentials e Opaque Tokens - 40", 10'
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients
			.inMemory()
				.withClient("algafood-web") //identificação do Client (aplicação que irá acessar os recursos do Resource Server) no AuthorizationServer que poderão solicitar um access token, 14' mostra a configuração no Postman
				.secret(passwordEncoder.encode("web123")) //4'30"
				.authorizedGrantTypes("password", "refresh_token") //22.9 - 5' , 22.13. Configurando o Refresh Token Grant Type no Authorization Server - 50"
				.scopes("write", "read") //6'
				.accessTokenValiditySeconds(6 * 60 * 60) // 6 horas (padrão é 12 horas) - 19'
				.refreshTokenValiditySeconds(60 * 24 * 60 * 60) //22.14. Configurando a validade e não reutilização de Refresh Tokens - 1' - O defeult do tempo do access Token é de 30 dias 
			.and()
				.withClient("checktoken")
					.secret(passwordEncoder.encode("check123")); //22.11. Configurando o Resource Server com a nova stack do Spring Security - 8', 15'
	}
	
	//22.10. Configurando o endpoint de introspecção de tokens no Authorization Server - 5'
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//		security.checkTokenAccess("isAuthenticated()");
		security.checkTokenAccess("permitAll()");
	}
	
	//22.9. Configurando o fluxo Authorization Server com Password Credentials e Opaque Tokens - 11'20"
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.authenticationManager(authenticationManager)
			.userDetailsService(userDetailsService) //22.13. Configurando o Refresh Token Grant Type no Authorization Server - 5'
			.reuseRefreshTokens(false); //22.14. Configurando a validade e não reutilização de Refresh Tokens - 4' - O default é o uso do refresh token, deixando false, não vai reutilizar. 
			
	}
}
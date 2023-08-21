package com.algaworks.algafood.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
//22.8. Criando o projeto do Authorization Server com Spring Security OAuth2 - 12'

@Configuration
@EnableAuthorizationServer
@SuppressWarnings("deprecation")
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager; //essa injeção não existe no contexto do Spring, precisa configurar no WebSecurityConfig - 11'20" 
	
	//22.9. Configurando o fluxo Authorization Server com Password Credentials e Opaque Tokens - 40", 10'
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients
			.inMemory()
				.withClient("algafood-web") //identificação do Client no AuthorizationServer, 14' mostra a configuração no Postman
				.secret(passwordEncoder.encode("web123")) //4'30"
				.authorizedGrantTypes("password") //5'
				.scopes("write", "read") //6'
				.accessTokenValiditySeconds(60 * 60 * 6) // 6 horas (padrão é 12 horas) - 19'
			.and()
				.withClient("checktoken")
					.secret(passwordEncoder.encode("check123")); //22.11. Configurando o Resource Server com a nova stack do Spring Security - 15'
	}
	
	//22.10. Configurando o endpoint de introspecção de tokens no Authorization Server - 5'
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//		security.checkTokenAccess("isAuthenticated()");
		security.checkTokenAccess("permitAll()");
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager);
	}
}

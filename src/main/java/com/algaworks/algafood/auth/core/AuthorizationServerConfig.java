package com.algaworks.algafood.auth.core;

//***Foi retirada a configuração do REDIS nessa aula, criei a classe AuthorizationServerConfig_OLD para manter a configuração para consulta.
//23.5. Gerando JWT com chave simétrica (HMAC SHA-256) no Authorization Server - 1'30"

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
//22.8. Criando o projeto do Authorization Server com Spring Security OAuth2 - 12'

@Configuration
@EnableAuthorizationServer //***Habilita o projeto para ser um Authorization Server
@SuppressWarnings("deprecation")
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private JwtKeyStoreProperties jwtKeyStoreProperties; //23.10. Desafio: criando bean de propriedades de configuração do KeyStore 
	
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
				.scopes("WRITE", "READ") //6'
				.accessTokenValiditySeconds(6 * 60 * 60) // 6 horas (padrão é 12 horas) - 19'
				.refreshTokenValiditySeconds(60 * 24 * 60 * 60) //22.14. Configurando a validade e não reutilização de Refresh Tokens - 1' - O defeult do tempo do access Token é de 30 dias 
			.and() //22.18. Configurando o Authorization Code Grant Type - 7' -> http://localhost:8081/oauth/authorize?response_type=code&client_id=foodanalytics&state=abc&redirect_uri=http://aplicacao-cliente
				.withClient("foodanalytics")
//				.secret(passwordEncoder.encode("food123"))
				.secret(passwordEncoder.encode("")) //22.24. Testando o fluxo Authorization Code com PKCE com o método plain 11'10", 12'20" - Explicação sobre não usar secret quando usa PKCE
				.authorizedGrantTypes("authorization_code")
				.scopes("WRITE", "READ")
				.redirectUris("http://localhost:8082") //22.19. Testando o fluxo Authorization Code com um client JavaScript - 3'
			.and() //22.16. Configurando o Client Credentials Grant Type no Authorization Server - 1'
				.withClient("faturamento")
				.secret(passwordEncoder.encode("faturamento123"))
				.authorizedGrantTypes("client_credentials")
				.scopes("WRITE", "READ")
			.and()//22.21. Configurando o fluxo Implicit Grant Type
				.withClient("webadmin")
				.authorizedGrantTypes("implicit")
				.scopes("WRITE", "READ")
				.redirectUris("http://aplicacao-cliente")				
			.and()
				.withClient("checktoken")
					.secret(passwordEncoder.encode("check123")); //22.11. Configurando o Resource Server com a nova stack do Spring Security - 8', 15'
	}
	
	//22.10. Configurando o endpoint de introspecção de tokens no Authorization Server - 5'
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//		security.checkTokenAccess("isAuthenticated()");
		security.checkTokenAccess("permitAll()")
		.tokenKeyAccess("permitAll()") //23.11. Extraindo a chave pública no formato PEM - 10'30"
		.allowFormAuthenticationForClients(); //22.24. Testando o fluxo Authorization Code com PKCE com o método plain 11'10", 12'20" - Explicação sobre não usar secret quando usa PKCE
	}
	
	//22.9. Configurando o fluxo Authorization Server com Password Credentials e Opaque Tokens - 11'20"
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		var enhancerChain = new TokenEnhancerChain(); //23.16. Adicionando claims customizadas no payload do JWT - 10'20"
		enhancerChain.setTokenEnhancers(
				Arrays.asList(new JwtCustomClaimsTokenEnhancer(), jwtAccessTokenConverter()));
		
		
		endpoints
			.authenticationManager(authenticationManager)
			.userDetailsService(userDetailsService) //22.13. Configurando o Refresh Token Grant Type no Authorization Server - 5'
			.reuseRefreshTokens(false) //22.14. Configurando a validade e não reutilização de Refresh Tokens - 4' - O default é o uso do refresh token, deixando false, não vai reutilizar.
			.accessTokenConverter(jwtAccessTokenConverter()) //23.5. Gerando JWT com chave simétrica (HMAC SHA-256) no Authorization Server - 6'30"
			.tokenEnhancer(enhancerChain) //23.16. Adicionando claims customizadas no payload do JWT - 10'20"
			.approvalStore(approvalStore(endpoints.getTokenStore())) //23.13. Revisando o fluxo de aprovação do Authorization Code com JWT - 1'50"
			.tokenGranter(tokenGranter(endpoints)); //22.23. Implementando o suporte a PKCE com o fluxo Authorization Code 4'50"
	}

	//23.13. Revisando o fluxo de aprovação do Authorization Code com JWT - 2'50"
	private ApprovalStore approvalStore(TokenStore tokenStore) {
		var approvalStore = new TokenApprovalStore();
		approvalStore.setTokenStore(tokenStore);
		return approvalStore;
	}
	
	//23.9. Assinando o JWT com RSA SHA-256 (chave assimétrica) - 2'10"
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		var jwtAccessTokenConverter = new JwtAccessTokenConverter();
		
		var jksResource = new ClassPathResource(jwtKeyStoreProperties.getPath());
		var keyStorePass = jwtKeyStoreProperties.getPassword();
		var keyPairAlias = jwtKeyStoreProperties.getKeypairAlias();
		
		var keyStoreKeyFactory = new KeyStoreKeyFactory(jksResource, keyStorePass.toCharArray());
		var keyPair = keyStoreKeyFactory.getKeyPair(keyPairAlias);
		
		jwtAccessTokenConverter.setKeyPair(keyPair);
		
		return jwtAccessTokenConverter;
	}
	
	//23.5. Gerando JWT com chave simétrica (HMAC SHA-256) no Authorization Server - 3'30", 12'
	/*
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		jwtAccessTokenConverter.setSigningKey("89a7sd89f7as98f7dsa98fds7fd89sasd9898asdf98s");//define um segredo para o token 
		
		return jwtAccessTokenConverter;
	}
	*/
	
	//22.23. Implementando o suporte a PKCE com o fluxo Authorization Code - 3'50"
	private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
		var pkceAuthorizationCodeTokenGranter = new PkceAuthorizationCodeTokenGranter(endpoints.getTokenServices(),
				endpoints.getAuthorizationCodeServices(), endpoints.getClientDetailsService(),
				endpoints.getOAuth2RequestFactory());
		
		var granters = Arrays.asList(
				pkceAuthorizationCodeTokenGranter, endpoints.getTokenGranter());
		
		return new CompositeTokenGranter(granters);
	}
	
/*
22.24. Testando o fluxo Authorization Code com PKCE com o método plain - 1', 4', 11'10", 12'20" - Explicação sobre não usar secret quando usa PKCE
Code Verifier: teste123
Code Challenge: teste123

http://localhost:8081/oauth/authorize?response_type=code&client_id=foodanalytics&redirect_uri=http://localhost:8082&code_challenge=teste123&code_challenge_method=plain



22.25. Testando o fluxo Authorization Code com PKCE com o método SHA-256
Code Verifier: teste123
Code Challenge: KJFg2w2fOfmuF1TE7JwW-QtQ4y4JxftUga5kKz09GjY

http://localhost:8081/oauth/authorize?response_type=code&client_id=foodanalytics&redirect_uri=http://localhost:8082&code_challenge=KJFg2w2fOfmuF1TE7JwW-QtQ4y4JxftUga5kKz09GjY&code_challenge_method=s256


https://tonyxu-io.github.io/pkce-generator/

 */
}

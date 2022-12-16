package com.algaworks.algafood;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

//10.3. Criando e rodando um teste de integração com Spring Boot, JUnit e AssertJ
//10.7. Configurando Maven Failsafe Plugin no projeto - 3'10" - Renomeado o nome da classe para sufixo IT (Integration test)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CadastroCozinhaIntegrationIT {

	//10.8. Implementando Testes de API com REST Assured e validando o código de status HTTP - 1'50", 9'30", 10'30" (importação estática)	
	@LocalServerPort
	private int port;
	
	@Autowired
	private Flyway flyway;
	
	//10.10. Criando um método para fazer setup dos testes
	@BeforeEach
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/cozinhas";
		
		flyway.migrate(); //10.12. Voltando o estado inicial do banco de dados para cada execução de teste com callback do Flyway
	}
	
	@Test
	public void deveRetornarStatus200_QuandoConsultarCozinhas() {				
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	//10.9. Validando o corpo da resposta HTTP
	@Test
	public void deveConter4Cozinhas_QuandoConsultarCozinhas() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", hasSize(4))
			.body("nome", hasItems("Indiana", "Tailandesa"));
	}
	
	//10.11. Entendendo o problema da ordem de execução dos testes
	@Test
	public void testRetornarStatus201_QuandoCadastrarCozinha() {
		given()
			.body("{ \"nome\": \"Chinesa\" }")
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
	
	
	
	
	
	/*
	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	@Test
	public void deveAtribuirId_QuandoCadastrarCozinhaComDadosCorretos() {
		// cenário
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome("Chinesa");
		
		// ação
		novaCozinha = cadastroCozinha.salvar(novaCozinha);
		
		// validação
		assertThat(novaCozinha).isNotNull();
		assertThat(novaCozinha.getId()).isNotNull();
	}
	
	@Test
	public void deveFalhar_QuandoCadastrarCozinhaSemNome() {
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome(null);
		
		   ConstraintViolationException erroEsperado =
				      Assertions.assertThrows(ConstraintViolationException.class, () -> {
				         cadastroCozinha.salvar(novaCozinha);
				      });
				   
				   assertThat(erroEsperado).isNotNull();
	}

	@Test
	public void deveFalhar_QuandoExcluirCozinhaEmUso() {
		EntidadeEmUsoException erroEsperado =
				Assertions.assertThrows(EntidadeEmUsoException.class, () -> {
					cadastroCozinha.excluir(1L);
				});

		assertThat(erroEsperado).isNotNull();

	}

	@Test
	public void deveFalhar_QuandoExcluirCozinhaInexistente() {
		CozinhaNaoEncontradaException erroEsperado =
				Assertions.assertThrows(CozinhaNaoEncontradaException.class, () -> {
					cadastroCozinha.excluir(100L);
				});

		assertThat(erroEsperado).isNotNull();

	}
	*/
}

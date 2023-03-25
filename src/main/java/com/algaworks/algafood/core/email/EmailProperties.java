package com.algaworks.algafood.core.email;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

//15.3. Implementando o serviço de infraestrutura de envio de e-mails com Spring - 8'40"

@Validated
@Getter
@Setter
@Component
@ConfigurationProperties("algafood.email")
public class EmailProperties {

	@NotNull
	private String remetente;
	
	private Sandbox sandbox = new Sandbox(); //15.9. Desafio: Implementando serviço de envio de e-mail sandbox
	
	//15.8. Desafio: implementando serviço de envio de e-mail fake
	// Atribuimos FAKE como padrão
	// Isso evita o problema de enviar e-mails de verdade caso você esqueça
	// de definir a propriedade
	private Implementacao impl = Implementacao.FAKE;

	public enum Implementacao {
	    SMTP, FAKE, SANDBOX
	}
	
	@Getter
	@Setter
	public class Sandbox {
		
		private String destinatario;
		
	}
}

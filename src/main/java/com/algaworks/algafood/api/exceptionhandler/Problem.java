package com.algaworks.algafood.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;

//8.12. Tratando exceções em nível de controlador com @ExceptionHandler - 7'50", 9'
@JsonInclude(Include.NON_NULL) //8.18. Padronizando o formato de problemas no corpo de respostas com a RFC 7807 - 5'30", só incluirá as propriedades que não forem nulas
@Getter
@Builder
public class Problem {

	//8.18. Padronizando o formato de problemas no corpo de respostas com a RFC 7807 - 1'
	private Integer status;
	private String type;
	private String title;
	private String detail;
	
	private String userMessage; //8.28. Estendendo o formato do problema para adicionar novas propriedades
	//private LocalDateTime timestamp; //8.29. Desafio: estendendo o formato do problema
	private OffsetDateTime timestamp; //11.8. Desafio: refatorando o código para usar OffsetDateTime
		
	//private List<Field> fields; //9.4. Estendendo o Problem Details para adicionar as propriedades com constraints violadas - 1'
	private List<Object> objects; //9.18. Ajustando Exception Handler para adicionar mensagens de validação em nível de classe - 6'30"
	
	@Getter
	@Builder
	public static class Object {
		private String name;
		private String userMessage;
	}
}

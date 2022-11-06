package com.algaworks.algafood.api.exceptionhandler;

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
	
}

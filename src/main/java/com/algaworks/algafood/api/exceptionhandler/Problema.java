package com.algaworks.algafood.api.exceptionhandler;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

//8.12. Tratando exceções em nível de controlador com @ExceptionHandler - 7'50", 9'
@Getter
@Builder
public class Problema {

	private LocalDateTime dataHora;
	private String mensagem;
	
}

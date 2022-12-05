package com.algaworks.algafood.core.validation;

import org.springframework.validation.BindingResult;

import lombok.AllArgsConstructor;
import lombok.Getter;

//9.19. Executando processo de validação programaticamente - 9'20"

@AllArgsConstructor
@Getter
public class ValidacaoException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	private BindingResult bindingResult;
	
}

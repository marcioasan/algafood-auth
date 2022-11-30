package com.algaworks.algafood.core.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

//9.16. Criando constraints de validação customizadas com implementação de ConstraintValidator - 1'30", 12'15"

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = { MultiploValidator.class })
public @interface Multiplo {


	String message() default "múltiplo inválido";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
	
	//9.16. Criando constraints de validação customizadas com implementação de ConstraintValidator - 3'
	int numero();
}

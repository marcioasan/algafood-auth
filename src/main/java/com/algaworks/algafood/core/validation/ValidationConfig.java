package com.algaworks.algafood.core.validation;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

//9.14. Usando o Resource Bundle do Spring como Resource Bundle do Bean Validation - 3', 5'30"

@Configuration
public class ValidationConfig {

	public LocalValidatorFactoryBean validator(MessageSource messageSource) {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource); //configura para usar o messages.properties, se tirar essa linha, vai usar o ValidationMessages.properties
		return bean;
	}
}

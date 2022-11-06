package com.algaworks.algafood.api.exceptionhandler;

import lombok.Getter;

//8.18. Padronizando o formato de problemas no corpo de respostas com a RFC 7807 - 13'30"
@Getter
public enum ProblemType {

	ENTIDADE_NAO_ENCONTRADA("/entidade-nao-encontrada", "Entidade não encontrada");
	
	private String title;
	private String uri;
	
	private ProblemType(String path, String title) {
		this.uri = "https://algafood.com.br" + path;
		this.title = title;
	}
	
	
}

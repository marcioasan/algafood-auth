package com.algaworks.algafood.core.web;

import org.springframework.http.MediaType;

//*** 20.13. Implementando o versionamento da API por URI - retirado nessa aula 5'

//20.10. Preparando o projeto para versionamento da API por Media Type - 11'

public class AlgaMediaTypes_NAO_USADO {

	public static final String V1_APPLICATION_JSON_VALUE = "application/vnd.algafood.v1+json";

	public static final MediaType V1_APPLICATION_JSON = MediaType.valueOf(V1_APPLICATION_JSON_VALUE);
	
	//20.11. Implementando o versionamento da API por Media Type - 1'
	public static final String V2_APPLICATION_JSON_VALUE = "application/vnd.algafood.v2+json";

	public static final MediaType V2_APPLICATION_JSON = MediaType.valueOf(V2_APPLICATION_JSON_VALUE);
}

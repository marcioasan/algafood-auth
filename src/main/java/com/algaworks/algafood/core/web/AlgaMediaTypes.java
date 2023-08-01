package com.algaworks.algafood.core.web;

import org.springframework.http.MediaType;

//20.10. Preparando o projeto para versionamento da API por Media Type - 11'

public class AlgaMediaTypes {

	public static final String V1_APPLICATION_JSON_VALUE = "application/vnd.algafood.v1+json";

	public static final MediaType V1_APPLICATION_JSON = MediaType.valueOf(V1_APPLICATION_JSON_VALUE);
}

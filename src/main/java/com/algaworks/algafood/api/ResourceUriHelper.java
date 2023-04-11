package com.algaworks.algafood.api;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.experimental.UtilityClass;

//19.2. Adicionando a URI do recurso criado no header da resposta - 11'pode me
@UtilityClass
public class ResourceUriHelper {

	public static void addUriInResponseHeader(Object resourceId) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri() //19.2. Adicionando a URI do recurso criado no header da resposta - 5'
			.path("/{id}")
			.buildAndExpand(resourceId).toUri();
		
		HttpServletResponse response = ((ServletRequestAttributes) 
				RequestContextHolder.getRequestAttributes()).getResponse();
		
		response.setHeader(HttpHeaders.LOCATION, uri.toString());
	}
}

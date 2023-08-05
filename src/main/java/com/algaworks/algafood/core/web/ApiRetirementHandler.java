package com.algaworks.algafood.core.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

//20.19. Desligando uma versão da API

@Component
public class ApiRetirementHandler implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, 
	                         HttpServletResponse response, Object handler)
			throws Exception {
		//Comentei esse trecho para continuar usando a v1
//		if (request.getRequestURI().startsWith("/v1/")) {
//			response.setStatus(HttpStatus.GONE.value());
//			return false;
//		}

		return true;
	}
}

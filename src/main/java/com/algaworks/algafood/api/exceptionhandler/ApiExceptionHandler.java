package com.algaworks.algafood.api.exceptionhandler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;

//8.13. Tratando exceções globais com @ExceptionHandler e @ControllerAdvice - 1'28"
@ControllerAdvice //as exceções de todos os controllers serão tratadas aqui
public class ApiExceptionHandler {

    //8.12. Tratando exceções em nível de controlador com @ExceptionHandler
    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> tratarEntidadeNaoEncontradaException(EntidadeNaoEncontradaException e) {
    	
    	//8.12. Tratando exceções em nível de controlador com @ExceptionHandler - 9'
    	Problema problema = Problema.builder()
    			.dataHora(LocalDateTime.now())
    			.mensagem(e.getMessage()).build();
    	
    	return ResponseEntity.status(HttpStatus.NOT_FOUND)
    			.body(problema);
    }
    
    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> tratarNegocioException(EntidadeNaoEncontradaException e) {
    	
    	//8.12. Tratando exceções em nível de controlador com @ExceptionHandler - 9'
    	Problema problema = Problema.builder()
    			.dataHora(LocalDateTime.now())
    			.mensagem(e.getMessage()).build();
    	
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    			.body(problema);
    }
    
    //Se descomentar o método listarXml() de RestauranteController, ao executar o request listar no Postman será lançado HttpMediaTypeNotAcceptableException que será capturado aqui  
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<?> tratarHttpMediaTypeNotAcceptableException() {
    	
    	//8.12. Tratando exceções em nível de controlador com @ExceptionHandler - 9'
    	Problema problema = Problema.builder()
    			.dataHora(LocalDateTime.now())
    			.mensagem("Could not find acceptable representation").build();
    	
    	return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
    			.body(problema);
    }
}

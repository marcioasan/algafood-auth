package com.algaworks.algafood.api.exceptionhandler;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

//8.13. Tratando exceções globais com @ExceptionHandler e @ControllerAdvice - 1'28"
@ControllerAdvice //as exceções de todos os controllers serão tratadas aqui
public class ApiExceptionHandler extends ResponseEntityExceptionHandler { //8.15. Criando um exception handler global com ResponseEntityExceptionHandler

	//8.20. Customizando exception handlers de ResponseEntityExceptionHandler
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
    	
		//8.21. Tratando a exception InvalidFormatException na desserialização - 2', 3' (tem que adicionar a dependencia commons-lang3 no pom.xml), 6'
		Throwable rootCause = ExceptionUtils.getRootCause(ex);
		
		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
		}
		
		
    	ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
    	String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";
    	
    	Problem problem = createProblemBuilder(status, problemType, detail).build();
    	
    	return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	//8.21. Tratando a exception InvalidFormatException na desserialização - 4'30", 12'
    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
    	//8.21. Tratando a exception InvalidFormatException na desserialização - 13'		  
    	//for do Java 8
    	/*
    	ex.getPath().forEach(ref -> System.out.println(ref.getFieldName()));
  
    	//for no Java 5
    	List<Reference> path1 = ex.getPath(); 
    	for (Reference ref : path1) {
    		System.out.println(ref.getFieldName()); 
    	}
		*/
    	
		//8.21. Tratando a exception InvalidFormatException na desserialização - 15'
    	String path = ex.getPath().stream()
    			.map(ref -> ref.getFieldName())
    			.collect(Collectors.joining("."));
    	
    	ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
    	String detail = String.format("A propriedade '%s' recebeu o valor '%s',  que é de um tipo inválido."
    			+ " Corrija e informe um valor compatível com o tipo '%s'.", 
    			path, ex.getValue(), ex.getTargetType().getSimpleName());
    	
    	Problem problem = createProblemBuilder(status, problemType, detail).build();
    	
	return handleExceptionInternal(ex, problem, headers, status, request);
}

	//8.12. Tratando exceções em nível de controlador com @ExceptionHandler
    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex, WebRequest request) {
    	
    	//8.18. Padronizando o formato de problemas no corpo de respostas com a RFC 7807 - 19'40"
    	HttpStatus status = HttpStatus.NOT_FOUND;
    	ProblemType problemType = ProblemType.ENTIDADE_NAO_ENCONTRADA;
    	String detail = ex.getMessage();
    	
    	Problem problem = createProblemBuilder(status, problemType, detail).build();
    	
    	
    	//8.18. Padronizando o formato de problemas no corpo de respostas com a RFC 7807 - 7'
    	/*
    	HttpStatus status = HttpStatus.NOT_FOUND;
    	
    	Problem problem = Problem.builder()
    			.status(status.value())
    			.type("https://algafood.com.br/entidade-nao-encontrada")
    			.title("Entidade não encontrada")
    			.detail(ex.getMessage())
    			.build();
    	*/
    	
    	//8.16. Customizando o corpo da resposta padrão de ResponseEntityExceptionHandler - 5'
    	return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    	
    	//8.12. Tratando exceções em nível de controlador com @ExceptionHandler - 9'
    	/*
    	Problema problema = Problema.builder()
    			.dataHora(LocalDateTime.now())
    			.mensagem(e.getMessage()).build();
    	
    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problema);
    	*/
    }
    
  //8.14. Desafio: implementando exception handler
    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException ex, WebRequest request) {
    	
    	HttpStatus status = HttpStatus.CONFLICT;
    	ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
    	String detail = ex.getMessage();
    	
    	Problem problem = createProblemBuilder(status, problemType, detail).build();
    	
    	
    	//8.16. Customizando o corpo da resposta padrão de ResponseEntityExceptionHandler - 5'
    	return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }
    
    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> handleNegocioException(NegocioException ex, WebRequest request) {
    	
    	HttpStatus status = HttpStatus.BAD_REQUEST;
    	ProblemType problemType = ProblemType.ERRO_NEGOCIO;
    	String detail = ex.getMessage();
    	
    	Problem problem = createProblemBuilder(status, problemType, detail).build();

    	//8.16. Customizando o corpo da resposta padrão de ResponseEntityExceptionHandler - 5'
    	return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }
    
    //8.16. Customizando o corpo da resposta padrão de ResponseEntityExceptionHandler
    @Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		if (body == null) { //8.16. Customizando o corpo da resposta padrão de ResponseEntityExceptionHandler - 9'
			body = Problem.builder()
					.title(status.getReasonPhrase())
					.status(status.value())
					.build();			
		} else if(body instanceof String) {
			body = Problem.builder()
					.title((String) body)
					.status(status.value())
					.build();
		}
    	
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
    
    //8.18. Padronizando o formato de problemas no corpo de respostas com a RFC 7807 - 16'40"
    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail){
    	
    	return Problem.builder()
    			.status(status.value())
    			.type(problemType.getUri())
    			.title(problemType.getTitle())
    			.detail(detail);
    }
    
    
    
    /*após estender ResponseEntityExceptionHandler, não precisa dessa implementação - //8.15. Criando um exception handler global com ResponseEntityExceptionHandler
    //Se comentar o método listarXml() de RestauranteController, ao executar o request listar no Postman será lançado HttpMediaTypeNotAcceptableException que será capturado aqui  
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<?> handleHttpMediaTypeNotAcceptableException() {
    	
    	//8.12. Tratando exceções em nível de controlador com @ExceptionHandler - 9'
    	Problema problema = Problema.builder()
    			.dataHora(LocalDateTime.now())
    			.mensagem("Could not find acceptable representation").build();
    	
    	return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
    			.body(problema);
    }
    */
    
    /* após estender ResponseEntityExceptionHandler, não precisa dessa implementação - //8.15. Criando um exception handler global com ResponseEntityExceptionHandler
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> tratarHttpMediaTypeNotSupportedException() {
    	
    	//8.12. Tratando exceções em nível de controlador com @ExceptionHandler - 9'
    	Problema problema = Problema.builder()
    			.dataHora(LocalDateTime.now())
    			.mensagem("Content type 'application/json' not supported").build();
    	
    	return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    			.body(problema);
    }
    */
}

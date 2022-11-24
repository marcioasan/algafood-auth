package com.algaworks.algafood.api.exceptionhandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

//8.13. Tratando exceções globais com @ExceptionHandler e @ControllerAdvice - 1'28"
@ControllerAdvice //as exceções de todos os controllers serão tratadas aqui
public class ApiExceptionHandler extends ResponseEntityExceptionHandler { 
	
	private static final String MSG_ERRO_GENERICA_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema. "
	+ "Tente novamente e se o problema persistir, entre em contato com o administrador do sistema.";

//8.15. Criando um exception handler global com ResponseEntityExceptionHandler

	//8.27. Desafio: tratando outras exceções não capturadas
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
	    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;		
	    ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
	    String detail = MSG_ERRO_GENERICA_USUARIO_FINAL;

	    // Importante colocar o printStackTrace (pelo menos por enquanto, que não estamos
	    // fazendo logging) para mostrar a stacktrace no console
	    // Se não fizer isso, você não vai ver a stacktrace de exceptions que seriam importantes
	    // para você durante, especialmente na fase de desenvolvimento
	    ex.printStackTrace();
	    
	    Problem problem = createProblemBuilder(status, problemType, detail)
	    		.userMessage(detail)
	    		.build();

	    return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ProblemType problemType = ProblemType.DADOS_INVALIDOS;
		String detail = String.format("Um ou mais campos são inválidos. Faça o preenchimento correto e tente novamente", ex.getFieldError());
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(detail)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	//8.26. Desafio: tratando a exceção NoHandlerFoundException
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
	    ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
	    String detail = String.format("O recurso %s, que você tentou acessar, é inexistente.", ex.getRequestURL());
	    
	    Problem problem = createProblemBuilder(status, problemType, detail)
	    		.userMessage(detail)
	    		.build();
	    
	    return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	//8.25. Desafio: tratando exception de parâmetro de URL inválido
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
	    
	    if (ex instanceof MethodArgumentTypeMismatchException) {
	        return handleMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) ex, headers, status, request);
	    }

	    return super.handleTypeMismatch(ex, headers, status, request);
	}
	
	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpHeaders headers, 
			HttpStatus status, WebRequest request) {

	    ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;

	    String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', "
	            + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
	            ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

	    Problem problem = createProblemBuilder(status, problemType, detail)
	    		.userMessage(detail)
	    		.build();

	    return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	//8.20. Customizando exception handlers de ResponseEntityExceptionHandler
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
    	
		//8.21. Tratando a exception InvalidFormatException na desserialização - 2', 3' (tem que adicionar a dependencia commons-lang3 no pom.xml), 6'
		Throwable rootCause = ExceptionUtils.getRootCause(ex);
		
		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
		} else if(rootCause instanceof PropertyBindingException) {
			return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request);
		}
		
    	ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
    	String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";
    	
    	Problem problem = createProblemBuilder(status, problemType, detail)
    			.userMessage(detail)
    			.build();
    	
    	return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	//8.21. Tratando a exception InvalidFormatException na desserialização - 4'30", 12'
    private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex,
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
    	String path = joinPath(ex.getPath());    	
    	
    	ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
    	String detail = String.format("A propriedade '%s' recebeu o valor '%s',  que é de um tipo inválido."
    			+ " Corrija e informe um valor compatível com o tipo '%s'.", 
    			path, ex.getValue(), ex.getTargetType().getSimpleName());
    	
    	Problem problem = createProblemBuilder(status, problemType, detail)
    			.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL) //8.28. Estendendo o formato do problema para adicionar novas propriedades - 6'30"
    			.build();
    	
	return handleExceptionInternal(ex, problem, headers, status, request);
}

    //8.23. Desafio: tratando a PropertyBindingException na desserialização
    private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        // Criei o método joinPath para reaproveitar em todos os métodos que precisam
        // concatenar os nomes das propriedades (separando por ".")
        String path = joinPath(ex.getPath());
        
        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = String.format("A propriedade '%s' não existe. "
                + "Corrija ou remova essa propriedade e tente novamente.", path);

        Problem problem = createProblemBuilder(status, problemType, detail)
        		.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL) //8.28. Estendendo o formato do problema para adicionar novas propriedades - 6'30"
        		.build();
        
        return handleExceptionInternal(ex, problem, headers, status, request);
    } 
    
    private String joinPath(List<Reference> references) {
    	//8.21. Tratando a exception InvalidFormatException na desserialização - 15'
    	return references.stream()
            .map(ref -> ref.getFieldName())
            .collect(Collectors.joining("."));
    } 
    
	//8.12. Tratando exceções em nível de controlador com @ExceptionHandler
    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex, WebRequest request) {
    	
    	//8.18. Padronizando o formato de problemas no corpo de respostas com a RFC 7807 - 19'40"
    	HttpStatus status = HttpStatus.NOT_FOUND;
    	ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
    	String detail = ex.getMessage();
    	
    	Problem problem = createProblemBuilder(status, problemType, detail)
    			.userMessage(detail)
    			.build();
    	
    	
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
    public ResponseEntity<?> handleEntidadeEmUso(EntidadeEmUsoException ex, WebRequest request) {
    	
    	HttpStatus status = HttpStatus.CONFLICT;
    	ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
    	String detail = ex.getMessage();
    	
    	Problem problem = createProblemBuilder(status, problemType, detail)
    			.userMessage(detail) //8.28. Estendendo o formato do problema para adicionar novas propriedades - 9'30"
    			.build();
    	
    	
    	//8.16. Customizando o corpo da resposta padrão de ResponseEntityExceptionHandler - 5'
    	return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }
    
    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> handleNegocio(NegocioException ex, WebRequest request) {
    	
    	HttpStatus status = HttpStatus.BAD_REQUEST;
    	ProblemType problemType = ProblemType.ERRO_NEGOCIO;
    	String detail = ex.getMessage();
    	
    	Problem problem = createProblemBuilder(status, problemType, detail)
    			.userMessage(detail)
    			.build();

    	//8.16. Customizando o corpo da resposta padrão de ResponseEntityExceptionHandler - 5'
    	return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }
    
    //8.16. Customizando o corpo da resposta padrão de ResponseEntityExceptionHandler
    @Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		if (body == null) { //8.16. Customizando o corpo da resposta padrão de ResponseEntityExceptionHandler - 9'
			body = Problem.builder()
					.timestamp(LocalDateTime.now())
					.title(status.getReasonPhrase())
					.status(status.value())
					.build();			
		} else if(body instanceof String) {
			body = Problem.builder()
					.timestamp(LocalDateTime.now())
					.title((String) body)
					.status(status.value())
					.build();
		}
    	
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
    
    //8.18. Padronizando o formato de problemas no corpo de respostas com a RFC 7807 - 16'40"
    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail){
    	
    	return Problem.builder()
    			.timestamp(LocalDateTime.now()) //8.29. Desafio: estendendo o formato do problema
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

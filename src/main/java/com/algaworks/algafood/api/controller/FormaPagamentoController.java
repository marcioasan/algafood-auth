package com.algaworks.algafood.api.controller;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.algaworks.algafood.api.assembler.FormaPagamentoInputDisassembler;
import com.algaworks.algafood.api.assembler.FormaPagamentoModelAssembler;
import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.api.model.input.FormaPagamentoInput;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;
import com.algaworks.algafood.domain.service.CadastroFormaPagamentoService;

@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {

	   @Autowired
	    private FormaPagamentoRepository formaPagamentoRepository;
	    
	    @Autowired
	    private CadastroFormaPagamentoService cadastroFormaPagamento;
	    
	    @Autowired
	    private FormaPagamentoModelAssembler formaPagamentoModelAssembler;
	    
	    @Autowired
	    private FormaPagamentoInputDisassembler formaPagamentoInputDisassembler;
	    
	    
	    
	    //17.9. Implementando requisições condicionais com Deep ETags - 1'30", 7', 9'30"
		@GetMapping
		public ResponseEntity<List<FormaPagamentoModel>> listar(ServletWebRequest request) {
			ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
			
			String eTag = "0";
			
			OffsetDateTime dataUltimaAtualizacao = formaPagamentoRepository.getDataUltimaAtualizacao();
			
			if (dataUltimaAtualizacao != null) {
				eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
			}
			
			if (request.checkNotModified(eTag)) {
				return null;
			}

			//Outra forma de retorno para não devolver nulo conforme é feito no trecho acima
//			if (request.checkNotModified(eTag)) {
//				return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
//			               .cacheControl(CacheControl.maxAge(0, TimeUnit.SECONDS).cachePublic())
//			               .eTag(eTag)
//			               .build();
//			}
			
			List<FormaPagamento> todasFormasPagamentos = formaPagamentoRepository.findAll();
			
			List<FormaPagamentoModel> formasPagamentosModel = formaPagamentoModelAssembler
					.toCollectionModel(todasFormasPagamentos);
			
			return ResponseEntity.ok()
					.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
					.eTag(eTag)
					.body(formasPagamentosModel);
		}
	    
	    /*
	    //17.8. Entendendo e preparando a implementação de Deep ETags - 4'
	    //17.6. Adicionando outras diretivas de Cache-Control na resposta HTTP - 30", 2'20", 3'50" - cachePrivate e cachePublic
	    //17.2. Habilitando o cache com o cabeçalho Cache-Control e a diretiva max-age - 2'40", 9'40" fala do Talend API Tester para testar cache em uma aplicação pelo browser, pois não dá pra testar cache pelo Postman
	    @GetMapping
	    public ResponseEntity<List<FormaPagamentoModel>> listar() {
	        List<FormaPagamento> todasFormasPagamentos = formaPagamentoRepository.findAll();
	        
	        List<FormaPagamentoModel> formasPagamentoModel = formaPagamentoModelAssembler.toCollectionModel(todasFormasPagamentos);
	        
	        return ResponseEntity.ok()
//	        		.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePrivate())
//	        		.cacheControl(CacheControl.noCache())
//	        		.cacheControl(CacheControl.noStore())
	        		.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
	        		.body(formasPagamentoModel);
	    }
	    */
	    
//	    @GetMapping
//	    public List<FormaPagamentoModel> listar() {
//	        List<FormaPagamento> todasFormasPagamentos = formaPagamentoRepository.findAll();
//	        
//	        return formaPagamentoModelAssembler.toCollectionModel(todasFormasPagamentos);
//	    }

	    //17.3. Desafio: adicionando o cabeçalho Cache-Control na resposta
	    @GetMapping("/{formaPagamentoId}")
	    public ResponseEntity<FormaPagamentoModel> buscar(@PathVariable Long formaPagamentoId) {
	      FormaPagamento formaPagamento = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);
	      
	      FormaPagamentoModel formaPagamentoModel =  formaPagamentoModelAssembler.toModel(formaPagamento);
	      
	      return ResponseEntity.ok()
	          .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
	          .body(formaPagamentoModel);
	    }
	    
//	    @GetMapping("/{formaPagamentoId}")
//	    public FormaPagamentoModel buscar(@PathVariable Long formaPagamentoId) {
//	        FormaPagamento formaPagamento = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);
//	        
//	        return formaPagamentoModelAssembler.toModel(formaPagamento);
//	    }
	    
	    @PostMapping
	    @ResponseStatus(HttpStatus.CREATED)
	    public FormaPagamentoModel adicionar(@RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
	        FormaPagamento formaPagamento = formaPagamentoInputDisassembler.toDomainObject(formaPagamentoInput);
	        
	        formaPagamento = cadastroFormaPagamento.salvar(formaPagamento);
	        
	        return formaPagamentoModelAssembler.toModel(formaPagamento);
	    }
	    
	    @PutMapping("/{formaPagamentoId}")
	    public FormaPagamentoModel atualizar(@PathVariable Long formaPagamentoId,
	            @RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
	        FormaPagamento formaPagamentoAtual = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);
	        
	        formaPagamentoInputDisassembler.copyToDomainObject(formaPagamentoInput, formaPagamentoAtual);
	        
	        formaPagamentoAtual = cadastroFormaPagamento.salvar(formaPagamentoAtual);
	        
	        return formaPagamentoModelAssembler.toModel(formaPagamentoAtual);
	    }
	    
	    @DeleteMapping("/{formaPagamentoId}")
	    @ResponseStatus(HttpStatus.NO_CONTENT)
	    public void remover(@PathVariable Long formaPagamentoId) {
	        cadastroFormaPagamento.excluir(formaPagamentoId);	
	    }
}

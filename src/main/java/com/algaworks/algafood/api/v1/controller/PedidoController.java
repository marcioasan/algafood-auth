package com.algaworks.algafood.api.v1.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.assembler.PedidoInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.PedidoModelAssembler;
import com.algaworks.algafood.api.v1.assembler.PedidoResumoModelAssembler;
import com.algaworks.algafood.api.v1.model.PedidoModel;
import com.algaworks.algafood.api.v1.model.PedidoResumoModel;
import com.algaworks.algafood.api.v1.model.input.PedidoInput;
import com.algaworks.algafood.core.data.PageWrapper;
import com.algaworks.algafood.core.data.PageableTranslator;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.filter.PedidoFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.repository.spec.PedidoSpecs;
import com.algaworks.algafood.domain.service.EmissaoPedidoService;
import com.google.common.collect.ImmutableMap;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private EmissaoPedidoService emissaoPedido;
    
    @Autowired
    private PedidoModelAssembler pedidoModelAssembler;
    
    @Autowired
    private PedidoResumoModelAssembler pedidoResumoModelAssembler; 
    
    @Autowired
    private PedidoInputDisassembler pedidoInputDisassembler;
    
    @Autowired
    private PagedResourcesAssembler<Pedido> pagedResourcesAssembler;
    
    //19.16. Desafio: adicionando hypermedia em recursos de pedidos (paginação)
    @GetMapping
    public PagedModel<PedidoResumoModel> pesquisar(PedidoFilter filtro, 
            @PageableDefault(size = 10) Pageable pageable) {
        Pageable pageableTraduzido = traduzirPageable(pageable); //19.17. Corrigindo links de paginação com ordenação - 10'30"
        
        Page<Pedido> pedidosPage = pedidoRepository.findAll(
                PedidoSpecs.usandoFiltro(filtro), pageableTraduzido);
        
        pedidosPage = new PageWrapper<>(pedidosPage, pageable);
        
        return pagedResourcesAssembler.toModel(pedidosPage, pedidoResumoModelAssembler);
    }
    
    /*
    //13.9. Desafio: implementando paginação e ordenação de pedidos
    @GetMapping
    public Page<PedidoResumoModel> pesquisar(PedidoFilter filtro, 
            @PageableDefault(size = 10) Pageable pageable) {
    	//13.11. Implementando um conversor de propriedades de ordenação - 7'30"
    	pageable = traduzirPageable(pageable);
    	
    	
        Page<Pedido> pedidosPage = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(filtro), pageable);
        
        List<PedidoResumoModel> pedidosResumoModel = pedidoResumoModelAssembler.toCollectionModel(pedidosPage.getContent());
        
        Page<PedidoResumoModel> pedidosResumoModelPage = new PageImpl<>(pedidosResumoModel, pageable, pedidosPage.getTotalElements());
        
        return pedidosResumoModelPage;
    }
    */
    
    /*
	@GetMapping
	public List<PedidoResumoModel> pesquisar(PedidoFilter filtro) { //13.6. Implementando pesquisas complexas na API - 7'50", mudou o nome do método de listar() para pesquisar
		List<Pedido> todosPedidos = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(filtro)); //13.6. Implementando pesquisas complexas na API - 9'
		
		return pedidoResumoModelAssembler.toCollectionModel(todosPedidos);
	}
	*/
    
    /* Nessa aula, foi feito esse método configurando os filtros programaticamente, precisa usar a anotação @JsonFilter("pedidoFilter") em PedidoResumoModel
    //13.2. Limitando os campos retornados pela API com @JsonFilter do Jackson - 4', 8'20", 9'30"
	@GetMapping
	public MappingJacksonValue listar(@RequestParam(required = false) String campos) {
		List<Pedido> pedidos = pedidoRepository.findAll();
		List<PedidoResumoModel> pedidosModel = pedidoResumoModelAssembler.toCollectionModel(pedidos);  
		
		MappingJacksonValue pedidosWrapper = new MappingJacksonValue(pedidosModel);
		
		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
		filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.serializeAll()); //pedidoFilter é o parâmetro configurado na anotação @JsonFilter("pedidoFilter") em PedidoResumoModel
//		filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.filterOutAllExcept("codigo", "valorTotal")); //devolve somente essas propriedades
		
		if (StringUtils.isNotBlank(campos)) {
			filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.filterOutAllExcept(campos.split(",")));
		}
		
		pedidosWrapper.setFilters(filterProvider);
		
		return pedidosWrapper;
	}
	*/    
    
    @GetMapping("/{codigoPedido}") //12.25. Usando IDs vs UUIDs nas URIs de recursos - 16'20"
    public PedidoModel buscar(@PathVariable String codigoPedido) {
        Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
        
        return pedidoModelAssembler.toModel(pedido);
    }
    
    //12.21. Desafio: Implementando o endpoint de emissão de pedidos
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoModel adicionar(@Valid @RequestBody PedidoInput pedidoInput) {
        try {
            Pedido novoPedido = pedidoInputDisassembler.toDomainObject(pedidoInput);

            // TODO pegar usuário autenticado
            novoPedido.setCliente(new Usuario());
            novoPedido.getCliente().setId(1L); //usando id fixo até chegar a parte de autenticação para pegar usuário autenticado 6'30"

            novoPedido = emissaoPedido.emitir(novoPedido);

            return pedidoModelAssembler.toModel(novoPedido);
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }
    
    //13.11. Implementando um conversor de propriedades de ordenação - 6'50", 9'50" -> no lugar de ImmutableMap poderia ser Map.of da própria API do Java que aceita até 10 chaves-valores
	private Pageable traduzirPageable(Pageable apiPageable) {
		var mapeamento = ImmutableMap.of(
				"codigo", "codigo",
				"restaurante.nome", "restaurante.nome",
				"nomeCliente", "cliente.nome",
				"valorTotal", "valorTotal"
			);
		
		return PageableTranslator.translate(apiPageable, mapeamento);
	}
}

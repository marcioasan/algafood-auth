package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.PedidoInputDisassembler;
import com.algaworks.algafood.api.assembler.PedidoModelAssembler;
import com.algaworks.algafood.api.assembler.PedidoResumoModelAssembler;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.api.model.input.PedidoInput;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.repository.filter.PedidoFilter;
import com.algaworks.algafood.domain.repository.spec.PedidoSpecs;
import com.algaworks.algafood.domain.service.EmissaoPedidoService;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

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
    
	@GetMapping
	public List<PedidoResumoModel> pesquisar(PedidoFilter filtro) { //13.6. Implementando pesquisas complexas na API - 7'50", mudou o nome do método de listar() para pesquisar
		List<Pedido> todosPedidos = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(filtro)); //13.6. Implementando pesquisas complexas na API - 9'
		
		return pedidoResumoModelAssembler.toCollectionModel(todosPedidos);
	}
    
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
}

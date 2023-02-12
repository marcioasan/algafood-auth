package com.algaworks.algafood.infrastructure.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.algaworks.algafood.api.controller.dto.VendaDiaria;
import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.service.VendaQueryService;

//13.13. Discutindo sobre onde implementar as consultas com dados agregados - 12'

@Repository
public class VendaQueryServiceImpl implements VendaQueryService {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro) { //13.14. Implementando consulta com dados agregados de vendas diárias - 3'30", 9'10", 13'50"
		var builder = manager.getCriteriaBuilder();
		var query = builder.createQuery(VendaDiaria.class); //VendaDiaria é o tipo de retorno da query, não é a entidade do banco
		var root = query.from(Pedido.class);
		
		var functionDateDataCriacao = builder.function(
				"date", Date.class, root.get("dataCriacao"));
		
		var selection = builder.construct(VendaDiaria.class,
				functionDateDataCriacao,
				builder.count(root.get("id")),
				builder.sum(root.get("valorTotal")));
		
		query.select(selection);
		query.groupBy(functionDateDataCriacao);
		
		return manager.createQuery(query).getResultList();
	}

}

/*
 --13.14. Implementando consulta com dados agregados de vendas diárias - 3'
select date(p.data_criacao) as data_criacao,
	count(p.id) as total_vendas,
    sum(p.valor_total) as total_faturado
from pedido p
group by date(p.data_criacao);  
 */ 

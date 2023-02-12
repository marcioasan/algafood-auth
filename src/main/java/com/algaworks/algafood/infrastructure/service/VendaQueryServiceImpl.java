package com.algaworks.algafood.infrastructure.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Repository;

import com.algaworks.algafood.api.controller.dto.VendaDiaria;
import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.StatusPedido;
import com.algaworks.algafood.domain.service.VendaQueryService;

//13.13. Discutindo sobre onde implementar as consultas com dados agregados - 12'

@Repository
public class VendaQueryServiceImpl implements VendaQueryService {

	@PersistenceContext
	private EntityManager manager;

	//13.15. Desafio: adicionando os filtros na consulta de vendas diárias
	//13.14. Implementando consulta com dados agregados de vendas diárias - 3'30", 9'10", 13'50"
	@Override
	public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffset) {
		var builder = manager.getCriteriaBuilder();
		var query = builder.createQuery(VendaDiaria.class); //VendaDiaria é o tipo de retorno da query, não é a entidade do banco
		var root = query.from(Pedido.class);
		var predicates = new ArrayList<Predicate>();
		
		//13.16. Tratando time offset na agregação de vendas diárias por data - 7'30"
		var functionConvertTzDataCriacao = builder.function(
				"convert_tz", Date.class, root.get("dataCriacao"), 
				builder.literal("+00:00"), builder.literal(timeOffset));
		
		
		var functionDateDataCriacao = builder.function(
				"date", Date.class, functionConvertTzDataCriacao);
		
		var selection = builder.construct(VendaDiaria.class,
				functionDateDataCriacao,
				builder.count(root.get("id")),
				builder.sum(root.get("valorTotal")));
		
		if (filtro.getRestauranteId() != null) {
			predicates.add(builder.equal(root.get("restaurante"), filtro.getRestauranteId()));
		}
	      
		if (filtro.getDataCriacaoInicio() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"), 
					filtro.getDataCriacaoInicio()));
		}

		if (filtro.getDataCriacaoFim() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), 
					filtro.getDataCriacaoFim()));
		}
	      
		predicates.add(root.get("status").in(
				StatusPedido.CONFIRMADO, StatusPedido.ENTREGUE));
		
		query.select(selection);
		query.where(predicates.toArray(new Predicate[0]));
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

/*
--13.16. Tratando time offset na agregação de vendas diárias por data - 5'

select convert_tz('2019-11-03 02:00:30', '+00:00', '-03:00'); --converte a data '2019-11-03 02:00:30' que está em UTC para o timezone de Brasília

select date(convert_tz(p.data_criacao, '+00:00', '-03:00')) as data_criacao,
	count(p.id) as total_vendas,
    sum(p.valor_total) as total_faturado
from pedido p
where p.status in ('CONFIRMADO', 'ENTREGUE')
group by date(convert_tz(p.data_criacao, '+00:00', '-03:00'));
 */


/*
 Tópico sobre configuração para Postgresql, não li tudo
 https://app.algaworks.com/forum/topicos/81826/funcao-convert_tz-no-postgresql
 
 Tópico sobre alternativa para consulta através de view no BD, não testei
 https://app.algaworks.com/forum/topicos/81639/aprendendo-e-contribuindo
 */



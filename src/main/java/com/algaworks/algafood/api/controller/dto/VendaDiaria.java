package com.algaworks.algafood.api.controller.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//13.13. Discutindo sobre onde implementar as consultas com dados agregados - 7'


@AllArgsConstructor
@Setter
@Getter
public class VendaDiaria {

	private Date data; //13.14. Implementando consulta com dados agregados de vendas diárias - 15', muda de LocalDate para Date para corrigir o erro --> Caused by: org.hibernate.hql.internal.ast.QuerySyntaxException: Unable to locate appropriate constructor on class [com.algaworks.algafood.api.controller.dto.VendaDiaria]. Expected arguments are: java.util.Date, long, java.math.BigDecimal [select new com.algaworks.algafood.api.controller.dto.VendaDiaria(function('date', generatedAlias0.dataCriacao), count(generatedAlias0.id), sum(generatedAlias0.valorTotal)) from com.algaworks.algafood.domain.model.Pedido as generatedAlias0 group by function('date', generatedAlias0.dataCriacao)] 
	private Long totalVendas;
	private BigDecimal totalFaturado;
}

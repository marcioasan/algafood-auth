package com.algaworks.algafood.domain.service;

import java.util.List;

import com.algaworks.algafood.api.controller.dto.VendaDiaria;
import com.algaworks.algafood.domain.filter.VendaDiariaFilter;

//13.13. Discutindo sobre onde implementar as consultas com dados agregados - 11' 

public interface VendaQueryService {

	//13.13. Discutindo sobre onde implementar as consultas com dados agregados - 8'30", 10'10", 10'45"
	List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffset);
}

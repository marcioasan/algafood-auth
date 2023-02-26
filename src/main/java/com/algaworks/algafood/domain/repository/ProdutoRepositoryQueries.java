package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.FotoProduto;

//14.6. Implementando serviço de cadastro de foto de produto - 3'50"
public interface ProdutoRepositoryQueries {

	FotoProduto save(FotoProduto foto);
	void delete(FotoProduto foto); //14.7. Excluindo e substituindo cadastro de foto de produto - 6'20"
}

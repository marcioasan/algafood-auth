package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.FotoProdutoModel;
import com.algaworks.algafood.domain.model.FotoProduto;

//14.6. Implementando serviço de cadastro de foto de produto - 11'20" 
@Component
public class FotoProdutoModelAssembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public FotoProdutoModel toModel(FotoProduto foto) {
		return modelMapper.map(foto, FotoProdutoModel.class);
	}
	
}

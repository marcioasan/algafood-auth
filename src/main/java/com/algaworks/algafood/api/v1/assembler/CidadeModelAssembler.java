package com.algaworks.algafood.api.v1.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.CidadeController;
import com.algaworks.algafood.api.v1.controller.EstadoController;
import com.algaworks.algafood.api.v1.model.CidadeModel;
import com.algaworks.algafood.domain.model.Cidade;

@Component
public class CidadeModelAssembler extends RepresentationModelAssemblerSupport<Cidade, CidadeModel>{ //19.11. Montando modelo de representação com RepresentationModelAssembler - 2'10", 3'10", 5'20", 6', 10', 12'

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private AlgaLinks algaLinks;  //19.21. Desafio: refatorando construção e inclusão de links
    
    //19.11. Montando modelo de representação com RepresentationModelAssembler - 3'10"
    public CidadeModelAssembler() {
	   super(CidadeController.class, CidadeModel.class);
    }
    
    @Override
    public CidadeModel toModel(Cidade cidade) {
        CidadeModel cidadeModel = createModelWithId(cidade.getId(), cidade);
        
        modelMapper.map(cidade, cidadeModel);
        
        cidadeModel.add(algaLinks.linkToCidades("cidades"));
        
        cidadeModel.getEstado().add(algaLinks.linkToEstado(cidadeModel.getEstado().getId()));
        
        return cidadeModel;
    }
    
	@Override
	public CollectionModel<CidadeModel> toCollectionModel(Iterable<? extends Cidade> entities) {
		return super.toCollectionModel(entities)
				.add(algaLinks.linkToCidades());
	}
}

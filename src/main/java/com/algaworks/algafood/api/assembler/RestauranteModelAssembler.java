package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.controller.RestauranteController;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.domain.model.Restaurante;

//19.24. Desafio: adicionando hypermedia nos recursos de restaurantes
@Component
public class RestauranteModelAssembler 
        extends RepresentationModelAssemblerSupport<Restaurante, RestauranteModel> {

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private AlgaLinks algaLinks;
    
    public RestauranteModelAssembler() {
        super(RestauranteController.class, RestauranteModel.class);
    }
    
    @Override
    public RestauranteModel toModel(Restaurante restaurante) {
        RestauranteModel restauranteModel = createModelWithId(restaurante.getId(), restaurante);
        modelMapper.map(restaurante, restauranteModel);
        
        restauranteModel.add(algaLinks.linkToRestaurantes("restaurantes"));
        
        restauranteModel.getCozinha().add(
                algaLinks.linkToCozinha(restaurante.getCozinha().getId()));
        
        restauranteModel.getEndereco().getCidade().add(
                algaLinks.linkToCidade(restaurante.getEndereco().getCidade().getId()));
        
        restauranteModel.add(algaLinks.linkToRestauranteFormasPagamento(restaurante.getId(), 
                "formas-pagamento"));
        
        restauranteModel.add(algaLinks.linkToResponsaveisRestaurante(restaurante.getId(), 
                "responsaveis"));
        
        //19.25. Desafio: adicionando links condicionais no recurso de restaurante - INÍCIO
        if (restaurante.ativacaoPermitida()) {
        	restauranteModel.add(
        			algaLinks.linkToRestauranteAtivacao(restaurante.getId(), "ativar"));
        }

        if (restaurante.inativacaoPermitida()) {
        	restauranteModel.add(
        			algaLinks.linkToRestauranteInativacao(restaurante.getId(), "inativar"));
        }

        if (restaurante.aberturaPermitida()) {
        	restauranteModel.add(
        			algaLinks.linkToRestauranteAbertura(restaurante.getId(), "abrir"));
        }

        if (restaurante.fechamentoPermitido()) {
        	restauranteModel.add(
        			algaLinks.linkToRestauranteFechamento(restaurante.getId(), "fechar"));
        }
      //19.25. Desafio: adicionando links condicionais no recurso de restaurante - FIM
        
        
        return restauranteModel;
    }
    
    @Override
    public CollectionModel<RestauranteModel> toCollectionModel(Iterable<? extends Restaurante> entities) {
        return super.toCollectionModel(entities)
                .add(algaLinks.linkToRestaurantes());
    }   
}



/* classe refatorada na aula 19.24
  //11.12. Refatorando e criando um assembler de DTO - 3'
  
  @Component public class RestauranteModelAssembler {
  
  @Autowired private ModelMapper modelMapper;
  
  //11.14. Adicionando e usando o ModelMapper - 5'45" public RestauranteModel
  toModel(Restaurante restaurante) { return modelMapper.map(restaurante,
  RestauranteModel.class); }
  
  
  //11.10. Implementando a conversão de entidade para DTO
  
  public RestauranteModel toModel(Restaurante restaurante) { CozinhaModel
  cozinhaModel = new CozinhaModel();
  cozinhaModel.setId(restaurante.getCozinha().getId());
  cozinhaModel.setNome(restaurante.getCozinha().getNome());
  
  RestauranteModel restauranteModel = new RestauranteModel();
  restauranteModel.setId(restaurante.getId());
  restauranteModel.setNome(restaurante.getNome());
  restauranteModel.setTaxaFrete(restaurante.getTaxaFrete());
  restauranteModel.setCozinha(cozinhaModel); return restauranteModel; }
  
  
  public List<RestauranteModel> toCollectionModel(List<Restaurante>
  restaurantes) { return restaurantes.stream() .map(restaurante ->
  toModel(restaurante)) .collect(Collectors.toList()); } }
 */
package com.algaworks.algafood.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class FotoProduto {

	@EqualsAndHashCode.Include
	@Id
	@Column(name = "produto_id")
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@MapsId //14.5. Mapeando entidade FotoProduto e relacionamento um-para-um - 5'50", 7'
	private Produto produto;
	
	private String nomeArquivo;
	private String descricao;
	private String contentType;
	private Long tamanho;
	
	//14.7. Excluindo e substituindo cadastro de foto de produto - 4'20"
	public Long getRestauranteId() {
		if (getProduto() != null) {
			return getProduto().getRestaurante().getId();
		}
		
		return null;
	}
}

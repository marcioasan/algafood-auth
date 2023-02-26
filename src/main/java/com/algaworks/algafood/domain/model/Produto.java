package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

//6.7. Desafio: mapeando relacionamento muitos-para-um

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	
	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private String descricao;
	
	@Column(nullable = false)
	private BigDecimal preco;
	
	@Column(nullable = false)
	private boolean ativo;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Restaurante restaurante;
	
	//14.5. Mapeando entidade FotoProduto e relacionamento um-para-um - 7'30"
	//*** Não é aconselhável fazer o mapeamento bidirecional quando é @OneToOne, ver o artigo abaixo
	//https://blog.algaworks.com/lazy-loading-com-mapeamento-onetoone/
//	@OneToOne(mappedBy = "produto")
//	private FotoProduto foto;
	
}

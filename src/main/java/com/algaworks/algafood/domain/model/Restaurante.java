package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

//3.15. Conhecendo e usando o Lombok - 9'
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurante {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String nome;
	
	@Column(name = "taxa_frete", nullable = false)
	private BigDecimal taxaFrete;

	//@JsonIgnore
	@JsonIgnoreProperties("hibernateLazyInitializer") //6.12. Alterando a estratégia de fetching para Lazy Loading 13'10" - ignora a propriedade do proxy da cozinha que foi criada em tempo de execução
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cozinha_id", nullable = false) //6.10. Entendendo o Eager Loading - 13' - Explicação sobre a consulta gerar um inner join com cozinha
	private Cozinha cozinha;
	
	@JsonIgnore //6.5. Testando e analisando o impacto da incorporação de classe na REST API - 5'
	@Embedded //6.4. Mapeando classes incorporáveis com @Embedded e @Embeddable - 6'50"
	private Endereco endereco;
	
	@JsonIgnore
	@CreationTimestamp //6.6. Mapeando propriedades com @CreationTimestamp e @UpdateTimestamp - 3', 8'40", 11'50" - essa anotação atribui uma data/hora para a propriedade dataCadastro quando ela for salva pela primeira vez 
	@Column(nullable = false, columnDefinition = "datetime")
	private LocalDateTime dataCadastro; //6.6. Mapeando propriedades com @CreationTimestamp e @UpdateTimestamp - 1'

	@JsonIgnore
	@UpdateTimestamp //6.6. Mapeando propriedades com @CreationTimestamp e @UpdateTimestamp - 4'20"
	@Column(nullable = false, columnDefinition = "datetime")
	private LocalDateTime dataAtualizacao;
	
	//@JsonIgnore //6.3. Analisando o impacto do relacionamento muitos-para-muitos na REST API - 4'50"	
	@ManyToMany //6.2. Mapeando relacionamento muitos-para-muitos com @ManyToMany - 4'50", 6'20", 6'50"
	//@ManyToMany(fetch = FetchType.EAGER) aula 6.14
	@JoinTable(name = "restaurante_forma_pagamento", 
		joinColumns = @JoinColumn(name = "restaurante_id"),
		inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id"))
	private List<FormaPagamento> formasPagamento = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "restaurante")
	private List<Produto> produtos =  new ArrayList<>(); //6.8. Desafio: mapeando relacionamento um-para-muitos
}


//*** por padrão, tudo que é toMany é lazy e toOne é Eager
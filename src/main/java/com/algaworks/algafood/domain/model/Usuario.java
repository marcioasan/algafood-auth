package com.algaworks.algafood.domain.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Usuario {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String senha;
	
	@CreationTimestamp //6.6. Mapeando propriedades com @CreationTimestamp e @UpdateTimestamp - 3', 8'40", 11'50" - essa anotação atribui uma data/hora para a propriedade dataCadastro quando ela for salva pela primeira vez
	@Column(nullable = false, columnDefinition = "datetime")
	//private LocalDateTime dataCadastro; //6.9. Desafio: mapeando relacionamentos muitos-para-muitos
	private OffsetDateTime dataCadastro; //11.8. Desafio: refatorando o código para usar OffsetDateTime
	
	@ManyToMany
	@JoinTable(name = "usuario_grupo",
	joinColumns = @JoinColumn(name = "usuario_id"),
	inverseJoinColumns = @JoinColumn(name = "grupo_id"))
	private List<Grupo> grupos = new ArrayList<>(); //6.9. Desafio: mapeando relacionamentos muitos-para-muitos
}

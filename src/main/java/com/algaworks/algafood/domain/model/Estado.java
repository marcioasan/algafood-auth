package com.algaworks.algafood.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.algaworks.algafood.Groups;

import lombok.Data;
import lombok.EqualsAndHashCode;

//3.15. Conhecendo e usando o Lombok - 9'
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Estado {

	@NotNull(groups = Groups.EstadoId.class) //9.9. Desafio: adicionando constraints de validação no modelo - ver anotações na classe RestauranteController.java
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Column(nullable = false)
	private String nome;	
}

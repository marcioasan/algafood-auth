package com.algaworks.algafood.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.algaworks.algafood.Groups;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;
import lombok.EqualsAndHashCode;


@JsonRootName("cozinha") //4.15. Customizando as representações XML e JSON com @JsonIgnore, @JsonProperty e @JsonRootName - 7'
@Data //3.15. Conhecendo e usando o Lombok - 9'
@EqualsAndHashCode(onlyExplicitlyIncluded = true) //3.15. Conhecendo e usando o Lombok - 8'50" - só gera o Equals e HashCode se deixar explícito
@Entity
public class Cozinha {

	@NotNull(groups = Groups.CozinhaId.class)  //9.6. Validando as associações de uma entidade em cascata - 2'30", //9.7. Agrupando e restringindo constraints que devem ser usadas na validação - 5' | 9.8. Convertendo grupos de constraints para validação em cascata com @ConvertGroup - 5'30" - Renomeou de Groups.CadastroRestaurante para Groups.CozinhaId  
	@EqualsAndHashCode.Include //3.15. Conhecendo e usando o Lombok - 8'50" - só gera o Equals e HashCode se deixar explícito - 9'30" - cria um Equals e HashCode usando apenas o id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	//4.15. Customizando as representações XML e JSON com @JsonIgnore, @JsonProperty e @JsonRootName - 3'30" - @JsonProperty muda o nome da representação que será devolvida para 'titulo', mas o nome no domínio continuará sendo 'nome'
	//@JsonIgnore
	//@JsonProperty("titulo")
	@Column(nullable = false)
	private String nome;
	
	//6.1. Mapeando relacionamento bidirecional com @OneToMany - 5', 10'10"
	@JsonIgnore
	@OneToMany(mappedBy = "cozinha")
	private List<Restaurante> restaurantes = new ArrayList<>();
}

package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;

import lombok.Data;
import lombok.EqualsAndHashCode;

//7.12. Desafio: Criando migrações e mapeando as entidades Pedido e ItemPedido

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
public class Pedido {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private BigDecimal subtotal;
	private BigDecimal taxaFrete;
	private BigDecimal valorTotal;
	
	@Embedded
	private Endereco enderecoEntrega; //6.4. Mapeando classes incorporáveis com @Embedded e @Embeddable - 4'30", 5'30"
	
	@Enumerated(EnumType.STRING)
	private StatusPedido status = StatusPedido.CRIADO; //12.19. Desafio: Implementando os endpoints de consulta de pedidos
	
//	@CreationTimestamp
//	private OffsetDateTime timestamp;
	
	private OffsetDateTime dataConfirmacao;
	private OffsetDateTime dataCancelamento;
	private OffsetDateTime dataEntrega;

	@ManyToOne
	@JoinColumn(nullable = false)
	private FormaPagamento formaPagamento;
	
	@ManyToOne
	@JoinColumn(nullable = false)	
	private Restaurante restaurante;
	
    @ManyToOne
    @JoinColumn(name = "usuario_cliente_id", nullable = false)
    private Usuario cliente;
	
	@OneToMany(mappedBy = "pedido")
	private List<ItemPedido> itens = new ArrayList<>();

	//12.19. Desafio: Implementando os endpoints de consulta de pedidos
	public void calcularValorTotal() {
	    
	      BigDecimal soma = BigDecimal.ZERO;
	      for (ItemPedido item : getItens()) {
	          BigDecimal preco = item.getPrecoTotal();
	          soma = soma.add(preco);
	      }
	      System.out.println("Soma= " + soma);
		
		this.subtotal = getItens().stream()
	        .map(item -> item.getPrecoTotal())
	        .reduce(BigDecimal.ZERO, BigDecimal::add);
	    
	    this.valorTotal = this.subtotal.add(this.taxaFrete);
	}

	public void definirFrete() {
	    setTaxaFrete(getRestaurante().getTaxaFrete());
	}

	public void atribuirPedidoAosItens() {
	    getItens().forEach(item -> item.setPedido(this));
	}
}

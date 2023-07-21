package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.AbstractAggregateRoot;

import com.algaworks.algafood.domain.event.PedidoCanceladoEvent;
import com.algaworks.algafood.domain.event.PedidoConfirmadoEvent;
import com.algaworks.algafood.domain.exception.NegocioException;

import lombok.Data;
import lombok.EqualsAndHashCode;

//7.12. Desafio: Criando migrações e mapeando as entidades Pedido e ItemPedido

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
public class Pedido extends AbstractAggregateRoot<Pedido>{ //15.11. Publicando Domain Events a partir do Aggregate Root - 2'10"

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//12.25. Usando IDs vs UUIDs nas URIs de recursos - 12'20"
	private String codigo;
	
	private BigDecimal subtotal;
	private BigDecimal taxaFrete;
	private BigDecimal valorTotal;
	
	@Embedded
	private Endereco enderecoEntrega; //6.4. Mapeando classes incorporáveis com @Embedded e @Embeddable - 4'30", 5'30"
	
	@Enumerated(EnumType.STRING)
	private StatusPedido status = StatusPedido.CRIADO; //12.19. Desafio: Implementando os endpoints de consulta de pedidos
	
	@CreationTimestamp
	private OffsetDateTime dataCriacao;
	
	private OffsetDateTime dataConfirmacao;
	private OffsetDateTime dataCancelamento;
	private OffsetDateTime dataEntrega;

	@ManyToOne(fetch = FetchType.LAZY) //12.20. Otimizando a query de pedidos e retornando model resumido na listagem - 10'10" - 
	@JoinColumn(nullable = false)
	private FormaPagamento formaPagamento;
	
	@ManyToOne
	@JoinColumn(nullable = false)	
	private Restaurante restaurante;
	
    @ManyToOne
    @JoinColumn(name = "usuario_cliente_id", nullable = false)
    private Usuario cliente;
	
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL) //12.21. Desafio: Implementando o endpoint de emissão de pedidos - 7'
	private List<ItemPedido> itens = new ArrayList<>();

	//12.21. Desafio: Implementando o endpoint de emissão de pedidos
	public void calcularValorTotal() {
	    getItens().forEach(ItemPedido::calcularPrecoTotal);
	    
	    this.subtotal = getItens().stream()
	        .map(item -> item.getPrecoTotal())
	        .reduce(BigDecimal.ZERO, BigDecimal::add);
	    
	    this.valorTotal = this.subtotal.add(this.taxaFrete);
	}
	
	//12.24. Refatorando o código de regras para transição de status de pedidos - 1', 2', 3'50"
	public void confirmar() {
		setStatus(StatusPedido.CONFIRMADO);
		setDataConfirmacao(OffsetDateTime.now());
		
		registerEvent(new PedidoConfirmadoEvent(this)); //15.11. Publicando Domain Events a partir do Aggregate Root - 3'40", 5'40"
	}
	
	public void entregar() {
		setStatus(StatusPedido.ENTREGUE);
		setDataEntrega(OffsetDateTime.now());
	}
	
	public void cancelar() {
		setStatus(StatusPedido.CANCELADO);
		setDataCancelamento(OffsetDateTime.now());
		
		registerEvent(new PedidoCanceladoEvent(this)); //15.14. Desafio: enviando e-mails no cancelamento de pedidos
	}
	
	//12.25. Usando IDs vs UUIDs nas URIs de recursos - 18'
	private void setStatus(StatusPedido novoStatus) {
		if (getStatus().naoPodeAlterarPara(novoStatus)) {
			throw new NegocioException(
					String.format("Status do pedido %s não pode ser alterado de %s para %s",
							getCodigo(), getStatus().getDescricao(), 
							novoStatus.getDescricao()));
		}
		
		this.status = novoStatus;
	}
	
	//19.23. Adicionando links condicionalmente
	public boolean podeSerConfirmado() {
		return getStatus().podeAlterarPara(StatusPedido.CONFIRMADO);
	}
	
	public boolean podeSerEntregue() {
		return getStatus().podeAlterarPara(StatusPedido.ENTREGUE);
	}
	
	public boolean podeSerCancelado() {
		return getStatus().podeAlterarPara(StatusPedido.CANCELADO);
	}
	
	@PrePersist
	private void gerarCodigo() {
		setCodigo(UUID.randomUUID().toString());
	}
	
	//12.19. Desafio: Implementando os endpoints de consulta de pedidos
	/*
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
   */
	
	/* Retirado na aula 12.21. Desafio: Implementando o endpoint de emissão de pedidos
	public void definirFrete() {
	    setTaxaFrete(getRestaurante().getTaxaFrete());
	}

	public void atribuirPedidoAosItens() {
	    getItens().forEach(item -> item.setPedido(this));
	}
	*/
}

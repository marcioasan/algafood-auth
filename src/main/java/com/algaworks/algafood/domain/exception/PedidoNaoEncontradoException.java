package com.algaworks.algafood.domain.exception;

public class PedidoNaoEncontradoException extends EntidadeNaoEncontradaException {

    private static final long serialVersionUID = 1L;

    //12.25. Usando IDs vs UUIDs nas URIs de recursos - 15'30", 15'50"
    
//    public PedidoNaoEncontradoException(String mensagem) {
//        super(mensagem);
//    }
    
    public PedidoNaoEncontradoException(String codigoPedido) {
        super(String.format("Não existe um pedido com código %s", codigoPedido)); //aos 15'50" troca o %d por %s porque o parâmetro da URL será uma string e não um Long
    }
}

package com.algaworks.algafood.api.v1.model.input;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

//12.5. Desafio: implementando os endpoints de formas de pagamento

@Setter
@Getter
public class FormaPagamentoInput {

    @NotBlank
    private String descricao;
}

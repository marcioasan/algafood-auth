//23.10. Desafio: criando bean de propriedades de configuração do KeyStore
//https://app.algaworks.com/forum/topicos/81729/hibernate-validator-nao-esta-mais-presente-na-2-3-1-versao-atual-do-springboot

package com.algaworks.algafood.auth.core;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated
@Component
@ConfigurationProperties("algafood.jwt.keystore")
public class JwtKeyStoreProperties {

    @NotBlank
    private String path;
    
    @NotBlank
    private String password;
    
    @NotBlank
    private String keypairAlias;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKeypairAlias() {
        return keypairAlias;
    }

    public void setKeypairAlias(String keypairAlias) {
        this.keypairAlias = keypairAlias;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

package com.example.msdesafiotecnico.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoResponseDto {

    private Long codigo;
    @JsonProperty("tipo_vinho")
    private String tipoVinho;
    private BigDecimal preco;
    private String safra;
    @JsonProperty("ano_compra")
    private Integer anoCompra;
}

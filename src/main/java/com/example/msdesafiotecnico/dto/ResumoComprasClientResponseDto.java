package com.example.msdesafiotecnico.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
public class ResumoComprasClientResponseDto {

    private String nomeCliente;
    private String cpfCliente;
    private Integer qtdTotalCompras;
    private BigDecimal valorTotalCompras;
}

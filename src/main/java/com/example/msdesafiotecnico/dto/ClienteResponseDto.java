package com.example.msdesafiotecnico.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClienteResponseDto {

    private String nome;
    private String cpf;
    private List<ClienteCompraResponseDto> compras;
}

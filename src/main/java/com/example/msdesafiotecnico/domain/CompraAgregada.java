package com.example.msdesafiotecnico.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CompraAgregada {
    private String nomeCliente;
    private String cpfCliente;
    private Long codigoProduto;
    private String tipoVinho;
    private String safra;
    private Integer anoCompra;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal valorTotal;

    // Construtor para Jackson
    @JsonCreator
    public CompraAgregada(
            @JsonProperty("nomeCliente") String nomeCliente,
            @JsonProperty("cpfCliente") String cpfCliente,
            @JsonProperty("codigoProduto") Long codigoProduto,
            @JsonProperty("tipoVinho") String tipoVinho,
            @JsonProperty("safra") String safra,
            @JsonProperty("anoCompra") Integer anoCompra,
            @JsonProperty("quantidade") Integer quantidade,
            @JsonProperty("precoUnitario") BigDecimal precoUnitario,
            @JsonProperty("valorTotal") BigDecimal valorTotal) {
        this.nomeCliente = nomeCliente;
        this.cpfCliente = cpfCliente;
        this.codigoProduto = codigoProduto;
        this.tipoVinho = tipoVinho;
        this.safra = safra;
        this.anoCompra = anoCompra;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.valorTotal = valorTotal;
    }

    public static CompraAgregada from(ClienteData cliente, CompraData compra, ProdutoData produto) {
        return CompraAgregada.builder()
                .nomeCliente(cliente.nome())
                .cpfCliente(cliente.cpf())
                .codigoProduto(produto.codigo())
                .tipoVinho(produto.tipoVinho())
                .safra(produto.safra())
                .anoCompra(produto.anoCompra())
                .quantidade(compra.quantidade())
                .precoUnitario(produto.preco())
                .valorTotal(produto.preco().multiply(BigDecimal.valueOf(compra.quantidade())))
                .build();
    }

    public record ClienteData(String nome, String cpf) {}
    public record CompraData(Long codigo, Integer quantidade) {}
    public record ProdutoData(Long codigo, String tipoVinho, String safra, Integer anoCompra, BigDecimal preco) {}
}

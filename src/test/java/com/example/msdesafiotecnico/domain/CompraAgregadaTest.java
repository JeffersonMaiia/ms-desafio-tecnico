package com.example.msdesafiotecnico.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CompraAgregadaTest {

    @Test
    void testCompraAgregada_Builder() {
        // When
        CompraAgregada compra = CompraAgregada.builder()
                .nomeCliente("João Silva")
                .cpfCliente("123.456.789-00")
                .codigoProduto(1L)
                .tipoVinho("Tinto")
                .safra("2020")
                .anoCompra(2023)
                .quantidade(2)
                .precoUnitario(BigDecimal.valueOf(50.00))
                .valorTotal(BigDecimal.valueOf(100.00))
                .build();

        // Then
        assertThat(compra).isNotNull();
        assertThat(compra.getNomeCliente()).isEqualTo("João Silva");
        assertThat(compra.getCpfCliente()).isEqualTo("123.456.789-00");
        assertThat(compra.getCodigoProduto()).isEqualTo(1L);
        assertThat(compra.getTipoVinho()).isEqualTo("Tinto");
        assertThat(compra.getSafra()).isEqualTo("2020");
        assertThat(compra.getAnoCompra()).isEqualTo(2023);
        assertThat(compra.getQuantidade()).isEqualTo(2);
        assertThat(compra.getPrecoUnitario()).isEqualTo(BigDecimal.valueOf(50.00));
        assertThat(compra.getValorTotal()).isEqualTo(BigDecimal.valueOf(100.00));
    }

    @Test
    void testCompraAgregada_From() {
        // Given
        CompraAgregada.ClienteData clienteData = new CompraAgregada.ClienteData("João Silva", "123.456.789-00");
        CompraAgregada.CompraData compraData = new CompraAgregada.CompraData(1L, 2);
        CompraAgregada.ProdutoData produtoData = new CompraAgregada.ProdutoData(1L, "Tinto", "2020", 2023, BigDecimal.valueOf(50.00));

        // When
        CompraAgregada compra = CompraAgregada.from(clienteData, compraData, produtoData);

        // Then
        assertThat(compra).isNotNull();
        assertThat(compra.getNomeCliente()).isEqualTo("João Silva");
        assertThat(compra.getCpfCliente()).isEqualTo("123.456.789-00");
        assertThat(compra.getCodigoProduto()).isEqualTo(1L);
        assertThat(compra.getTipoVinho()).isEqualTo("Tinto");
        assertThat(compra.getSafra()).isEqualTo("2020");
        assertThat(compra.getAnoCompra()).isEqualTo(2023);
        assertThat(compra.getQuantidade()).isEqualTo(2);
        assertThat(compra.getPrecoUnitario()).isEqualTo(BigDecimal.valueOf(50.00));
        assertThat(compra.getValorTotal()).isEqualTo(BigDecimal.valueOf(100.00));
    }

    @Test
    void testCompraAgregada_EqualsAndHashCode() {
        // Given
        CompraAgregada compra1 = CompraAgregada.builder()
                .nomeCliente("João Silva")
                .cpfCliente("123.456.789-00")
                .codigoProduto(1L)
                .tipoVinho("Tinto")
                .quantidade(2)
                .precoUnitario(BigDecimal.valueOf(50.00))
                .valorTotal(BigDecimal.valueOf(100.00))
                .build();

        CompraAgregada compra2 = CompraAgregada.builder()
                .nomeCliente("João Silva")
                .cpfCliente("123.456.789-00")
                .codigoProduto(1L)
                .tipoVinho("Tinto")
                .quantidade(2)
                .precoUnitario(BigDecimal.valueOf(50.00))
                .valorTotal(BigDecimal.valueOf(100.00))
                .build();

        // Then
        assertThat(compra1).isEqualTo(compra2);
        assertThat(compra1.hashCode()).isEqualTo(compra2.hashCode());
    }

    @Test
    void testCompraAgregada_ToString() {
        // Given
        CompraAgregada compra = CompraAgregada.builder()
                .nomeCliente("João Silva")
                .cpfCliente("123.456.789-00")
                .tipoVinho("Tinto")
                .build();

        // When
        String toString = compra.toString();

        // Then
        assertThat(toString).contains("João Silva");
        assertThat(toString).contains("123.456.789-00");
        assertThat(toString).contains("Tinto");
    }

    @Test
    void testClienteData_Record() {
        // When
        CompraAgregada.ClienteData clienteData = new CompraAgregada.ClienteData("João Silva", "123.456.789-00");

        // Then
        assertThat(clienteData.nome()).isEqualTo("João Silva");
        assertThat(clienteData.cpf()).isEqualTo("123.456.789-00");
    }

    @Test
    void testCompraData_Record() {
        // When
        CompraAgregada.CompraData compraData = new CompraAgregada.CompraData(1L, 2);

        // Then
        assertThat(compraData.codigo()).isEqualTo(1L);
        assertThat(compraData.quantidade()).isEqualTo(2);
    }

    @Test
    void testProdutoData_Record() {
        // When
        CompraAgregada.ProdutoData produtoData = new CompraAgregada.ProdutoData(1L, "Tinto", "2020", 2023, BigDecimal.valueOf(50.00));

        // Then
        assertThat(produtoData.codigo()).isEqualTo(1L);
        assertThat(produtoData.tipoVinho()).isEqualTo("Tinto");
        assertThat(produtoData.safra()).isEqualTo("2020");
        assertThat(produtoData.anoCompra()).isEqualTo(2023);
        assertThat(produtoData.preco()).isEqualTo(BigDecimal.valueOf(50.00));
    }

    @Test
    void testCompraAgregada_ValorTotalCalculation() {
        // Given
        CompraAgregada.ClienteData clienteData = new CompraAgregada.ClienteData("João Silva", "123.456.789-00");
        CompraAgregada.CompraData compraData = new CompraAgregada.CompraData(1L, 3);
        CompraAgregada.ProdutoData produtoData = new CompraAgregada.ProdutoData(1L, "Tinto", "2020", 2023, BigDecimal.valueOf(75.50));

        // When
        CompraAgregada compra = CompraAgregada.from(clienteData, compraData, produtoData);

        // Then
        BigDecimal expectedTotal = BigDecimal.valueOf(75.50).multiply(BigDecimal.valueOf(3));
        assertThat(compra.getValorTotal()).isEqualTo(expectedTotal);
    }
}

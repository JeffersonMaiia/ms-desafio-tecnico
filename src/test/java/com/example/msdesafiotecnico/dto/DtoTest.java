package com.example.msdesafiotecnico.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ResumoComprasClientResponseDtoTest {

    @Test
    void testResumoComprasClientResponseDto_GettersAndSetters() {
        // Given
        ResumoComprasClientResponseDto dto = new ResumoComprasClientResponseDto();

        // When
        dto.setNomeCliente("João Silva");
        dto.setCpfCliente("123.456.789-00");
        dto.setQtdTotalCompras(5);
        dto.setValorTotalCompras(BigDecimal.valueOf(250.00));

        // Then
        assertThat(dto.getNomeCliente()).isEqualTo("João Silva");
        assertThat(dto.getCpfCliente()).isEqualTo("123.456.789-00");
        assertThat(dto.getQtdTotalCompras()).isEqualTo(5);
        assertThat(dto.getValorTotalCompras()).isEqualTo(BigDecimal.valueOf(250.00));
    }

    @Test
    void testResumoComprasClientResponseDto_EqualsAndHashCode() {
        // Given
        ResumoComprasClientResponseDto dto1 = new ResumoComprasClientResponseDto();
        dto1.setNomeCliente("João Silva");
        dto1.setCpfCliente("123.456.789-00");
        dto1.setQtdTotalCompras(5);
        dto1.setValorTotalCompras(BigDecimal.valueOf(250.00));

        ResumoComprasClientResponseDto dto2 = new ResumoComprasClientResponseDto();
        dto2.setNomeCliente("João Silva");
        dto2.setCpfCliente("123.456.789-00");
        dto2.setQtdTotalCompras(5);
        dto2.setValorTotalCompras(BigDecimal.valueOf(250.00));

        // Then
        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }
}

class RecomendacaoProdutoResponseDtoTest {

    @Test
    void testRecomendacaoProdutoResponseDto_GettersAndSetters() {
        // Given
        RecomendacaoProdutoResponseDto dto = new RecomendacaoProdutoResponseDto();

        // When
        dto.setNomeCliente("Maria Santos");
        dto.setCpfCliente("987.654.321-00");
        dto.setTipoVinho("Tinto");

        // Then
        assertThat(dto.getNomeCliente()).isEqualTo("Maria Santos");
        assertThat(dto.getCpfCliente()).isEqualTo("987.654.321-00");
        assertThat(dto.getTipoVinho()).isEqualTo("Tinto");
    }
}

class ProdutoResponseDtoTest {

    @Test
    void testProdutoResponseDto_GettersAndSetters() {
        // Given
        ProdutoResponseDto dto = new ProdutoResponseDto();

        // When
        dto.setCodigo(1L);
        dto.setTipoVinho("Branco");
        dto.setPreco(BigDecimal.valueOf(75.50));
        dto.setSafra("2021");
        dto.setAnoCompra(2023);

        // Then
        assertThat(dto.getCodigo()).isEqualTo(1L);
        assertThat(dto.getTipoVinho()).isEqualTo("Branco");
        assertThat(dto.getPreco()).isEqualTo(BigDecimal.valueOf(75.50));
        assertThat(dto.getSafra()).isEqualTo("2021");
        assertThat(dto.getAnoCompra()).isEqualTo(2023);
    }
}

class ClienteResponseDtoTest {

    @Test
    void testClienteResponseDto_GettersAndSetters() {
        // Given
        ClienteResponseDto dto = new ClienteResponseDto();
        ClienteCompraResponseDto compra = new ClienteCompraResponseDto();
        compra.setCodigo(1L);
        compra.setQuantidade(2);

        // When
        dto.setNome("Carlos Oliveira");
        dto.setCpf("111.222.333-44");
        dto.setCompras(java.util.List.of(compra));

        // Then
        assertThat(dto.getNome()).isEqualTo("Carlos Oliveira");
        assertThat(dto.getCpf()).isEqualTo("111.222.333-44");
        assertThat(dto.getCompras()).hasSize(1);
        assertThat(dto.getCompras().get(0).getCodigo()).isEqualTo(1L);
        assertThat(dto.getCompras().get(0).getQuantidade()).isEqualTo(2);
    }
}

class ClienteCompraResponseDtoTest {

    @Test
    void testClienteCompraResponseDto_GettersAndSetters() {
        // Given
        ClienteCompraResponseDto dto = new ClienteCompraResponseDto();

        // When
        dto.setCodigo(10L);
        dto.setQuantidade(3);

        // Then
        assertThat(dto.getCodigo()).isEqualTo(10L);
        assertThat(dto.getQuantidade()).isEqualTo(3);
    }
}

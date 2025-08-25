package com.example.msdesafiotecnico.mapper;

import com.example.msdesafiotecnico.domain.CompraAgregada;
import com.example.msdesafiotecnico.dto.ResumoComprasClientResponseDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CompraMapperTest {

    private final CompraMapper mapper = CompraMapper.INSTANCE;

    @Test
    void testToResumoDto() {
        // Given
        CompraAgregada compra = CompraAgregada.builder()
                .nomeCliente("João Silva")
                .cpfCliente("123.456.789-00")
                .tipoVinho("Tinto")
                .quantidade(2)
                .valorTotal(BigDecimal.valueOf(100.00))
                .build();

        // When
        ResumoComprasClientResponseDto result = mapper.toResumoDto(compra);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNomeCliente()).isEqualTo("João Silva");
        assertThat(result.getCpfCliente()).isEqualTo("123.456.789-00");
        // qtdTotalCompras e valorTotalCompras são ignorados na anotação @Mapping
        assertThat(result.getQtdTotalCompras()).isNull();
        assertThat(result.getValorTotalCompras()).isNull();
    }

    @Test
    void testToResumoComTotais_Success() {
        // Given
        List<CompraAgregada> compras = createMockComprasAgregadas();

        // When
        ResumoComprasClientResponseDto result = mapper.toResumoComTotais(compras);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNomeCliente()).isEqualTo("João Silva");
        assertThat(result.getCpfCliente()).isEqualTo("123.456.789-00");
        assertThat(result.getQtdTotalCompras()).isEqualTo(5); // 2 + 3
        assertThat(result.getValorTotalCompras()).isEqualTo(BigDecimal.valueOf(250.00)); // 100 + 150
    }

    @Test
    void testToResumoComTotais_EmptyList() {
        // Given
        List<CompraAgregada> comprasVazias = List.of();

        // When
        ResumoComprasClientResponseDto result = mapper.toResumoComTotais(comprasVazias);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNomeCliente()).isNull();
        assertThat(result.getCpfCliente()).isNull();
        assertThat(result.getQtdTotalCompras()).isNull();
        assertThat(result.getValorTotalCompras()).isNull();
    }

    @Test
    void testToResumoComTotais_NullList() {
        // When
        ResumoComprasClientResponseDto result = mapper.toResumoComTotais(null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNomeCliente()).isNull();
        assertThat(result.getCpfCliente()).isNull();
        assertThat(result.getQtdTotalCompras()).isNull();
        assertThat(result.getValorTotalCompras()).isNull();
    }

    @Test
    void testToResumoComTotais_SingleItem() {
        // Given
        CompraAgregada compra = CompraAgregada.builder()
                .nomeCliente("Maria Santos")
                .cpfCliente("987.654.321-00")
                .quantidade(1)
                .valorTotal(BigDecimal.valueOf(75.00))
                .build();

        List<CompraAgregada> compras = List.of(compra);

        // When
        ResumoComprasClientResponseDto result = mapper.toResumoComTotais(compras);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNomeCliente()).isEqualTo("Maria Santos");
        assertThat(result.getCpfCliente()).isEqualTo("987.654.321-00");
        assertThat(result.getQtdTotalCompras()).isEqualTo(1);
        assertThat(result.getValorTotalCompras()).isEqualTo(BigDecimal.valueOf(75.00));
    }

    @Test
    void testToResumoComTotais_CalculationAccuracy() {
        // Given
        CompraAgregada compra1 = CompraAgregada.builder()
                .nomeCliente("Cliente Teste")
                .cpfCliente("111.111.111-11")
                .quantidade(1)
                .valorTotal(BigDecimal.valueOf(33.33))
                .build();

        CompraAgregada compra2 = CompraAgregada.builder()
                .nomeCliente("Cliente Teste")
                .cpfCliente("111.111.111-11")
                .quantidade(2)
                .valorTotal(BigDecimal.valueOf(66.67))
                .build();

        List<CompraAgregada> compras = List.of(compra1, compra2);

        // When
        ResumoComprasClientResponseDto result = mapper.toResumoComTotais(compras);

        // Then
        assertThat(result.getQtdTotalCompras()).isEqualTo(3);
        assertThat(result.getValorTotalCompras()).isEqualTo(BigDecimal.valueOf(100.00).setScale(2, RoundingMode.HALF_UP));
    }

    private List<CompraAgregada> createMockComprasAgregadas() {
        CompraAgregada compra1 = CompraAgregada.builder()
                .nomeCliente("João Silva")
                .cpfCliente("123.456.789-00")
                .quantidade(2)
                .valorTotal(BigDecimal.valueOf(100.00))
                .build();

        CompraAgregada compra2 = CompraAgregada.builder()
                .nomeCliente("João Silva")
                .cpfCliente("123.456.789-00")
                .quantidade(3)
                .valorTotal(BigDecimal.valueOf(150.00))
                .build();

        return List.of(compra1, compra2);
    }
}

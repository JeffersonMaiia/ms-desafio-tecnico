package com.example.msdesafiotecnico.service;

import com.example.msdesafiotecnico.domain.CompraAgregada;
import com.example.msdesafiotecnico.dto.RecomendacaoProdutoResponseDto;
import com.example.msdesafiotecnico.dto.ResumoComprasClientResponseDto;
import com.example.msdesafiotecnico.exception.NoContentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComprasServiceTest {

    @Mock
    private DataAggregationService dataAggregationService;

    private ComprasService comprasService;

    @BeforeEach
    void setUp() {
        comprasService = new ComprasService(dataAggregationService);
    }

    @Test
    void testFindAllCompras_Success() {
        // Given
        List<CompraAgregada> comprasAgregadas = createMockComprasAgregadas();
        when(dataAggregationService.getComprasAgregadas()).thenReturn(comprasAgregadas);

        // When
        List<ResumoComprasClientResponseDto> result = comprasService.findAllCompras();

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2); // 2 clientes distintos

        // Verifica ordenação por valor total crescente
        assertThat(result.get(0).getValorTotalCompras())
                .isLessThanOrEqualTo(result.get(1).getValorTotalCompras());

        verify(dataAggregationService).getComprasAgregadas();
    }

    @Test
    void testFindAllCompras_EmptyData() {
        // Given
        when(dataAggregationService.getComprasAgregadas()).thenReturn(List.of());

        // When & Then
        assertThatThrownBy(() -> comprasService.findAllCompras())
                .isInstanceOf(NoContentException.class)
                .hasMessage("Não foi possível encontrar dados de compras.");
    }

    @Test
    void testFindMaiorCompra_Success() {
        // Given
        List<CompraAgregada> comprasAgregadas = createMockComprasAgregadas();
        when(dataAggregationService.getComprasAgregadas()).thenReturn(comprasAgregadas);

        // When
        ResumoComprasClientResponseDto result = comprasService.findMaiorCompra(2023);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNomeCliente()).isNotNull();
        assertThat(result.getValorTotalCompras()).isGreaterThan(BigDecimal.ZERO);

        verify(dataAggregationService).getComprasAgregadas();
    }

    @Test
    void testFindMaiorCompra_AnoNaoEncontrado() {
        // Given
        List<CompraAgregada> comprasAgregadas = createMockComprasAgregadas();
        when(dataAggregationService.getComprasAgregadas()).thenReturn(comprasAgregadas);

        // When & Then
        assertThatThrownBy(() -> comprasService.findMaiorCompra(2020))
                .isInstanceOf(NoContentException.class)
                .hasMessageContaining("Não foi possível encontrar a maior compra realizada no ano de 2020");
    }

    @Test
    void testFindClientesFieis_Success() {
        // Given
        List<CompraAgregada> comprasAgregadas = createMockComprasComRecorrencia();
        when(dataAggregationService.getComprasAgregadas()).thenReturn(comprasAgregadas);

        // When
        List<ResumoComprasClientResponseDto> result = comprasService.findClientesFieis();

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSizeLessThanOrEqualTo(3); // Máximo 3 clientes

        // Verifica ordenação decrescente por quantidade e valor
        if (result.size() > 1) {
            assertThat(result.get(0).getQtdTotalCompras())
                    .isGreaterThanOrEqualTo(result.get(1).getQtdTotalCompras());
        }

        verify(dataAggregationService).getComprasAgregadas();
    }

    @Test
    void testRecomendacaoProdutos_Success() {
        // Given
        List<CompraAgregada> comprasAgregadas = createMockComprasAgregadas();
        when(dataAggregationService.getComprasAgregadas()).thenReturn(comprasAgregadas);

        // When
        List<RecomendacaoProdutoResponseDto> result = comprasService.recomendacaoProdutos();

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2); // 2 clientes distintos

        result.forEach(recomendacao -> {
            assertThat(recomendacao.getNomeCliente()).isNotNull();
            assertThat(recomendacao.getCpfCliente()).isNotNull();
            assertThat(recomendacao.getTipoVinho()).isNotNull();
        });

        verify(dataAggregationService).getComprasAgregadas();
    }

    @Test
    void testRecomendacaoProdutos_ClienteComMultiplosTipos() {
        // Given
        List<CompraAgregada> comprasAgregadas = createMockComprasComMultiplosTipos();
        when(dataAggregationService.getComprasAgregadas()).thenReturn(comprasAgregadas);

        // When
        List<RecomendacaoProdutoResponseDto> result = comprasService.recomendacaoProdutos();

        // Then
        assertThat(result).isNotEmpty();

        // Verifica se a recomendação é baseada no tipo mais comprado
        RecomendacaoProdutoResponseDto recomendacao = result.get(0);
        assertThat(recomendacao.getTipoVinho()).isEqualTo("Tinto"); // Tipo mais comprado nos mocks
    }

    @Test
    void testCalcularResumoCliente_ListaVazia() {
        // Given
        List<CompraAgregada> comprasVazias = List.of();
        when(dataAggregationService.getComprasAgregadas()).thenReturn(comprasVazias);

        // When & Then
        assertThatThrownBy(() -> comprasService.findAllCompras())
                .isInstanceOf(NoContentException.class);
    }

    private List<CompraAgregada> createMockComprasAgregadas() {
        CompraAgregada compra1 = CompraAgregada.builder()
                .nomeCliente("João Silva")
                .cpfCliente("123.456.789-00")
                .tipoVinho("Tinto")
                .quantidade(2)
                .precoUnitario(BigDecimal.valueOf(50.00))
                .valorTotal(BigDecimal.valueOf(100.00))
                .anoCompra(2023)
                .build();

        CompraAgregada compra2 = CompraAgregada.builder()
                .nomeCliente("Maria Santos")
                .cpfCliente("987.654.321-00")
                .tipoVinho("Branco")
                .quantidade(1)
                .precoUnitario(BigDecimal.valueOf(75.00))
                .valorTotal(BigDecimal.valueOf(75.00))
                .anoCompra(2023)
                .build();

        CompraAgregada compra3 = CompraAgregada.builder()
                .nomeCliente("Maria Santos")
                .cpfCliente("987.654.321-00")
                .tipoVinho("Rosé")
                .quantidade(3)
                .precoUnitario(BigDecimal.valueOf(60.00))
                .valorTotal(BigDecimal.valueOf(180.00))
                .anoCompra(2023)
                .build();

        return List.of(compra1, compra2, compra3);
    }

    private List<CompraAgregada> createMockComprasComRecorrencia() {
        CompraAgregada compra1 = CompraAgregada.builder()
                .nomeCliente("João Silva")
                .cpfCliente("123.456.789-00")
                .tipoVinho("Tinto")
                .quantidade(2)
                .precoUnitario(BigDecimal.valueOf(50.00))
                .valorTotal(BigDecimal.valueOf(100.00))
                .anoCompra(2023)
                .build();

        CompraAgregada compra2 = CompraAgregada.builder()
                .nomeCliente("João Silva")
                .cpfCliente("123.456.789-00")
                .tipoVinho("Tinto")
                .quantidade(1)
                .precoUnitario(BigDecimal.valueOf(50.00))
                .valorTotal(BigDecimal.valueOf(50.00))
                .anoCompra(2023)
                .build();

        CompraAgregada compra3 = CompraAgregada.builder()
                .nomeCliente("Maria Santos")
                .cpfCliente("987.654.321-00")
                .tipoVinho("Branco")
                .quantidade(1)
                .precoUnitario(BigDecimal.valueOf(75.00))
                .valorTotal(BigDecimal.valueOf(75.00))
                .anoCompra(2023)
                .build();

        return List.of(compra1, compra2, compra3);
    }

    private List<CompraAgregada> createMockComprasComMultiplosTipos() {
        CompraAgregada compra1 = CompraAgregada.builder()
                .nomeCliente("João Silva")
                .cpfCliente("123.456.789-00")
                .tipoVinho("Tinto")
                .quantidade(5)
                .precoUnitario(BigDecimal.valueOf(50.00))
                .valorTotal(BigDecimal.valueOf(250.00))
                .anoCompra(2023)
                .build();

        CompraAgregada compra2 = CompraAgregada.builder()
                .nomeCliente("João Silva")
                .cpfCliente("123.456.789-00")
                .tipoVinho("Branco")
                .quantidade(2)
                .precoUnitario(BigDecimal.valueOf(60.00))
                .valorTotal(BigDecimal.valueOf(120.00))
                .anoCompra(2023)
                .build();

        CompraAgregada compra3 = CompraAgregada.builder()
                .nomeCliente("João Silva")
                .cpfCliente("123.456.789-00")
                .tipoVinho("Rosé")
                .quantidade(1)
                .precoUnitario(BigDecimal.valueOf(70.00))
                .valorTotal(BigDecimal.valueOf(70.00))
                .anoCompra(2023)
                .build();

        return List.of(compra1, compra2, compra3);
    }
}

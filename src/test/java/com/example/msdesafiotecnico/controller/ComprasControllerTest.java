package com.example.msdesafiotecnico.controller;

import com.example.msdesafiotecnico.dto.RecomendacaoProdutoResponseDto;
import com.example.msdesafiotecnico.dto.ResumoComprasClientResponseDto;
import com.example.msdesafiotecnico.exception.NoContentException;
import com.example.msdesafiotecnico.service.ComprasService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ComprasController.class)
class ComprasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComprasService comprasService;

    @Test
    void testFindAllCompras_Success() throws Exception {
        // Given
        List<ResumoComprasClientResponseDto> mockCompras = createMockResumoCompras();
        when(comprasService.findAllCompras()).thenReturn(mockCompras);

        // When & Then
        mockMvc.perform(get("/v1/compras")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nomeCliente").value("João Silva"))
                .andExpect(jsonPath("$[0].cpfCliente").value("123.456.789-00"))
                .andExpect(jsonPath("$[0].qtdTotalCompras").value(2))
                .andExpect(jsonPath("$[0].valorTotalCompras").value(100.00));
    }

    @Test
    void testFindAllCompras_NoContent() throws Exception {
        // Given
        when(comprasService.findAllCompras()).thenThrow(new NoContentException("Não foi possível encontrar dados de compras."));

        // When & Then
        mockMvc.perform(get("/v1/compras")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.code").value("NO_CONTENT"));
    }

    @Test
    void testFindMaiorCompra_Success() throws Exception {
        // Given
        ResumoComprasClientResponseDto mockMaiorCompra = createMockResumoCompra();
        when(comprasService.findMaiorCompra(2023)).thenReturn(mockMaiorCompra);

        // When & Then
        mockMvc.perform(get("/v1/maior_compra/2023")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCliente").value("Maria Santos"))
                .andExpect(jsonPath("$.cpfCliente").value("987.654.321-00"))
                .andExpect(jsonPath("$.qtdTotalCompras").value(4))
                .andExpect(jsonPath("$.valorTotalCompras").value(255.00));
    }

    @Test
    void testFindMaiorCompra_NoContent() throws Exception {
        // Given
        when(comprasService.findMaiorCompra(2020)).thenThrow(new NoContentException("Não foi possível encontrar a maior compra realizada no ano de 2020."));

        // When & Then
        mockMvc.perform(get("/v1/maior_compra/2020")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.code").value("NO_CONTENT"));
    }

    @Test
    void testFindClientesFieis_Success() throws Exception {
        // Given
        List<ResumoComprasClientResponseDto> mockClientesFieis = createMockClientesFieis();
        when(comprasService.findClientesFieis()).thenReturn(mockClientesFieis);

        // When & Then
        mockMvc.perform(get("/v1/clientes_fieis")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].nomeCliente").value("Cliente Fiel 1"));
    }

    @Test
    void testFindClientesFieis_NoContent() throws Exception {
        // Given
        when(comprasService.findClientesFieis()).thenThrow(new NoContentException("Não foi possível encontrar dados de compras."));

        // When & Then
        mockMvc.perform(get("/v1/clientes_fieis")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.code").value("NO_CONTENT"));
    }

    @Test
    void testRecomendacaoProdutos_Success() throws Exception {
        // Given
        List<RecomendacaoProdutoResponseDto> mockRecomendacoes = createMockRecomendacoes();
        when(comprasService.recomendacaoProdutos()).thenReturn(mockRecomendacoes);

        // When & Then
        mockMvc.perform(get("/v1/recomendacao/cliente/tipo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nomeCliente").value("João Silva"))
                .andExpect(jsonPath("$[0].tipoVinho").value("Tinto"));
    }

    @Test
    void testRecomendacaoProdutos_NoContent() throws Exception {
        // Given
        when(comprasService.recomendacaoProdutos()).thenThrow(new NoContentException("Não foi possível encontrar dados de compras."));

        // When & Then
        mockMvc.perform(get("/v1/recomendacao/cliente/tipo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.code").value("NO_CONTENT"));
    }

    @Test
    void testInvalidPathParameter() throws Exception {
        // When & Then
        mockMvc.perform(get("/v1/maior_compra/invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("TYPE_MISMATCH"));
    }

    private List<ResumoComprasClientResponseDto> createMockResumoCompras() {
        ResumoComprasClientResponseDto cliente1 = new ResumoComprasClientResponseDto();
        cliente1.setNomeCliente("João Silva");
        cliente1.setCpfCliente("123.456.789-00");
        cliente1.setQtdTotalCompras(2);
        cliente1.setValorTotalCompras(BigDecimal.valueOf(100.00));

        ResumoComprasClientResponseDto cliente2 = new ResumoComprasClientResponseDto();
        cliente2.setNomeCliente("Maria Santos");
        cliente2.setCpfCliente("987.654.321-00");
        cliente2.setQtdTotalCompras(4);
        cliente2.setValorTotalCompras(BigDecimal.valueOf(255.00));

        return List.of(cliente1, cliente2);
    }

    private ResumoComprasClientResponseDto createMockResumoCompra() {
        ResumoComprasClientResponseDto cliente = new ResumoComprasClientResponseDto();
        cliente.setNomeCliente("Maria Santos");
        cliente.setCpfCliente("987.654.321-00");
        cliente.setQtdTotalCompras(4);
        cliente.setValorTotalCompras(BigDecimal.valueOf(255.00));
        return cliente;
    }

    private List<ResumoComprasClientResponseDto> createMockClientesFieis() {
        ResumoComprasClientResponseDto cliente1 = new ResumoComprasClientResponseDto();
        cliente1.setNomeCliente("Cliente Fiel 1");
        cliente1.setCpfCliente("111.111.111-11");
        cliente1.setQtdTotalCompras(10);
        cliente1.setValorTotalCompras(BigDecimal.valueOf(1000.00));

        ResumoComprasClientResponseDto cliente2 = new ResumoComprasClientResponseDto();
        cliente2.setNomeCliente("Cliente Fiel 2");
        cliente2.setCpfCliente("222.222.222-22");
        cliente2.setQtdTotalCompras(8);
        cliente2.setValorTotalCompras(BigDecimal.valueOf(800.00));

        ResumoComprasClientResponseDto cliente3 = new ResumoComprasClientResponseDto();
        cliente3.setNomeCliente("Cliente Fiel 3");
        cliente3.setCpfCliente("333.333.333-33");
        cliente3.setQtdTotalCompras(6);
        cliente3.setValorTotalCompras(BigDecimal.valueOf(600.00));

        return List.of(cliente1, cliente2, cliente3);
    }

    private List<RecomendacaoProdutoResponseDto> createMockRecomendacoes() {
        RecomendacaoProdutoResponseDto rec1 = new RecomendacaoProdutoResponseDto();
        rec1.setNomeCliente("João Silva");
        rec1.setCpfCliente("123.456.789-00");
        rec1.setTipoVinho("Tinto");

        RecomendacaoProdutoResponseDto rec2 = new RecomendacaoProdutoResponseDto();
        rec2.setNomeCliente("Maria Santos");
        rec2.setCpfCliente("987.654.321-00");
        rec2.setTipoVinho("Branco");

        return List.of(rec1, rec2);
    }
}

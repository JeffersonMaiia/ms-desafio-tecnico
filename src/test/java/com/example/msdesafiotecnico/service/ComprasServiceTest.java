package com.example.msdesafiotecnico.service;

import com.example.msdesafiotecnico.client.ClienteClient;
import com.example.msdesafiotecnico.client.ProdutoClient;
import com.example.msdesafiotecnico.dto.ClienteResponseDto;
import com.example.msdesafiotecnico.dto.ProdutoResponseDto;
import com.example.msdesafiotecnico.exception.NoContentException;
import com.example.msdesafiotecnico.util.MockObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ComprasServiceTest {

    @InjectMocks
    private ComprasService comprasService;

    @Mock
    private ClienteClient clienteClient;

    @Mock
    private ProdutoClient produtoClient;

    @Test
    @DisplayName("Método: findAllCompras - Deve retornar NoContentException quando não encontrar clientes ou produtos ")
    void deveRetornarNoContentExceptionQuandoNaoEncontrarClientesOuProdutosFindallCompras() {
        when(clienteClient.getClientes()).thenReturn(null);

        Throwable throwable = catchThrowable(() -> comprasService.findAllCompras());

        assertThat(throwable)
                .isNotNull()
                .hasMessage("Não foi possível encontrar clientes e produtos.")
                .isExactlyInstanceOf(NoContentException.class);
    }

    @Test
    @DisplayName("Método: findAllCompras - Deve retornar lista de ResumoComprasClientResponseDto")
    void deveRetornarListaDeResumoComprasClientResponseDto() throws Exception {
        when(clienteClient.getClientes())
                .thenReturn(MockObject.getListFromFile("json/ClienteResponseDto.json", ClienteResponseDto.class));
        when(produtoClient.getProdutos())
                .thenReturn(MockObject.getListFromFile("json/ProdutoResponseDto.json", ProdutoResponseDto.class));

        var resumoCompras = comprasService.findAllCompras();

        assertThat(resumoCompras)
                .isNotEmpty()
                .hasSize(2);
    }

    @Test
    @DisplayName("Método: findMaiorCompra - Deve retornar NoContentException quando não encontrar clientes ou produtos ")
    void deveRetornarNoContentExceptionQuandoNaoEncontrarClientesOuProdutosFindMaiorCompra() {
        when(clienteClient.getClientes()).thenReturn(null);

        Throwable throwable = catchThrowable(() -> comprasService.findMaiorCompra(2021));

        assertThat(throwable)
                .isNotNull()
                .hasMessage("Não foi possível encontrar clientes e produtos.")
                .isExactlyInstanceOf(NoContentException.class);
    }

    @Test
    @DisplayName("Método: findMaiorCompra - Deve retornar NoContentException quando não encontrar a maior compra realizada no ano")
    void deveRetornarNoContentExceptionQuandoNaoEncontrarMaiorCompraRealizadaNoAno() throws Exception {
        when(clienteClient.getClientes())
                .thenReturn(MockObject.getListFromFile("json/ClienteResponseDto.json", ClienteResponseDto.class));
        when(produtoClient.getProdutos())
                .thenReturn(MockObject.getListFromFile("json/ProdutoResponseDto.json", ProdutoResponseDto.class));

        Throwable throwable = catchThrowable(() -> comprasService.findMaiorCompra(2021));

        assertThat(throwable)
                .isNotNull()
                .hasMessage("Não foi possível encontrar a maior compra realizada no ano de 2021.")
                .isExactlyInstanceOf(NoContentException.class);
    }

    @Test
    @DisplayName("Método: findMaiorCompra - Deve retornar ResumoComprasClientResponseDto")
    void deveRetornarResumoComprasClientResponseDto() throws Exception {
        when(clienteClient.getClientes())
                .thenReturn(MockObject.getListFromFile("json/ClienteResponseDto.json", ClienteResponseDto.class));
        when(produtoClient.getProdutos())
                .thenReturn(MockObject.getListFromFile("json/ProdutoResponseDto.json", ProdutoResponseDto.class));

        var resumoCompras = comprasService.findMaiorCompra(2019);

        assertThat(resumoCompras).isNotNull();
        assertThat(resumoCompras.getValorTotalCompras())
                .isNotNull()
                .isEqualTo(new BigDecimal("632.50"));
    }

    @Test
    @DisplayName("Método: findClientesFieis - Deve retornar NoContentException quando não encontrar clientes ou produtos ")
    void deveRetornarNoContentExceptionQuandoNaoEncontrarClientesOuProdutosFindClientesFieis() {
        when(clienteClient.getClientes()).thenReturn(null);

        Throwable throwable = catchThrowable(() -> comprasService.findClientesFieis());

        assertThat(throwable)
                .isNotNull()
                .hasMessage("Não foi possível encontrar clientes e produtos.")
                .isExactlyInstanceOf(NoContentException.class);
    }

    @Test
    @DisplayName("Método: findClientesFieis - Deve retornar lista dos top 3 clientes mais fieis")
    void deveRetornarListaOs3ClientesMaisFieis() throws Exception {
        when(clienteClient.getClientes())
                .thenReturn(MockObject.getListFromFile("json/ClienteResponseDto.json", ClienteResponseDto.class));
        when(produtoClient.getProdutos())
                .thenReturn(MockObject.getListFromFile("json/ProdutoResponseDto.json", ProdutoResponseDto.class));

        var resumoCompras = comprasService.findClientesFieis();

        assertThat(resumoCompras)
                .isNotEmpty()
                .hasSize(2);
    }

    @Test
    @DisplayName("Método: recomendacaoProdutos - Deve retornar NoContentException quando não encontrar clientes ou produtos ")
    void deveRetornarNoContentExceptionQuandoNaoEncontrarClientesOuProdutosRecomendacaoProdutos() {
        when(clienteClient.getClientes()).thenReturn(null);

        Throwable throwable = catchThrowable(() -> comprasService.recomendacaoProdutos());

        assertThat(throwable)
                .isNotNull()
                .hasMessage("Não foi possível encontrar clientes e produtos.")
                .isExactlyInstanceOf(NoContentException.class);
    }

    @Test
    @DisplayName("Método: recomendacaoProdutos - Deve retornar lista de RecomendacaoProdutoResponseDto")
    void deveRetornarListaDeRecomendacaoProdutoResponseDto() throws Exception {
        when(clienteClient.getClientes())
                .thenReturn(MockObject.getListFromFile("json/ClienteResponseDto.json", ClienteResponseDto.class));
        when(produtoClient.getProdutos())
                .thenReturn(MockObject.getListFromFile("json/ProdutoResponseDto.json", ProdutoResponseDto.class));

        var recomendacaoProdutos = comprasService.recomendacaoProdutos();

        assertThat(recomendacaoProdutos)
                .isNotEmpty()
                .hasSize(2);
    }

}

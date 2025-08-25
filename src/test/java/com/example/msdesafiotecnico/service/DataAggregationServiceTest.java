package com.example.msdesafiotecnico.service;

import com.example.msdesafiotecnico.client.ClienteClient;
import com.example.msdesafiotecnico.client.ProdutoClient;
import com.example.msdesafiotecnico.domain.CompraAgregada;
import com.example.msdesafiotecnico.dto.ClienteCompraResponseDto;
import com.example.msdesafiotecnico.dto.ClienteResponseDto;
import com.example.msdesafiotecnico.dto.ProdutoResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataAggregationServiceTest {

    @Mock
    private ClienteClient clienteClient;

    @Mock
    private ProdutoClient produtoClient;

    private DataAggregationService dataAggregationService;

    @BeforeEach
    void setUp() {
        dataAggregationService = new DataAggregationService(clienteClient, produtoClient);
    }

    @Test
    void testGetComprasAgregadas_Success() {
        // Given
        List<ClienteResponseDto> clientes = createMockClientes();
        List<ProdutoResponseDto> produtos = createMockProdutos();

        when(clienteClient.getClientes()).thenReturn(clientes);
        when(produtoClient.getProdutos()).thenReturn(produtos);

        // When
        List<CompraAgregada> result = dataAggregationService.getComprasAgregadas();

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);

        CompraAgregada compra = result.get(0);
        assertThat(compra.getNomeCliente()).isEqualTo("João Silva");
        assertThat(compra.getCpfCliente()).isEqualTo("123.456.789-00");
        assertThat(compra.getTipoVinho()).isEqualTo("Tinto");
        assertThat(compra.getQuantidade()).isEqualTo(2);
        assertThat(compra.getValorTotal()).isEqualTo(BigDecimal.valueOf(100.00));

        verify(clienteClient).getClientes();
        verify(produtoClient).getProdutos();
    }

    @Test
    void testGetComprasAgregadas_ProdutoNaoEncontrado() {
        // Given
        List<ClienteResponseDto> clientes = createMockClientes();
        List<ProdutoResponseDto> produtos = List.of(); // Lista vazia

        when(clienteClient.getClientes()).thenReturn(clientes);
        when(produtoClient.getProdutos()).thenReturn(produtos);

        // When
        List<CompraAgregada> result = dataAggregationService.getComprasAgregadas();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void testBuscarClientesAsync_Success() {
        // Given
        List<ClienteResponseDto> clientes = createMockClientes();
        when(clienteClient.getClientes()).thenReturn(clientes);

        // When
        CompletableFuture<List<ClienteResponseDto>> future = dataAggregationService.buscarClientesAsync();

        // Then
        assertThat(future).isNotNull();
        assertThat(future.join()).isEqualTo(clientes);
        verify(clienteClient).getClientes();
    }

    @Test
    void testBuscarClientesAsync_Exception() {
        // Given
        when(clienteClient.getClientes()).thenThrow(new RuntimeException("API Error"));

        // When
        CompletableFuture<List<ClienteResponseDto>> future = dataAggregationService.buscarClientesAsync();

        // Then
        assertThat(future).isNotNull();
        assertThat(future.join()).isEmpty();
    }

    @Test
    void testBuscarProdutosAsync_Success() {
        // Given
        List<ProdutoResponseDto> produtos = createMockProdutos();
        when(produtoClient.getProdutos()).thenReturn(produtos);

        // When
        CompletableFuture<List<ProdutoResponseDto>> future = dataAggregationService.buscarProdutosAsync();

        // Then
        assertThat(future).isNotNull();
        assertThat(future.join()).isEqualTo(produtos);
        verify(produtoClient).getProdutos();
    }

    @Test
    void testBuscarProdutosAsync_Exception() {
        // Given
        when(produtoClient.getProdutos()).thenThrow(new RuntimeException("API Error"));

        // When
        CompletableFuture<List<ProdutoResponseDto>> future = dataAggregationService.buscarProdutosAsync();

        // Then
        assertThat(future).isNotNull();
        assertThat(future.join()).isEmpty();
    }

    @Test
    void testGetComprasAgregadas_MultipleClientes() {
        // Given
        List<ClienteResponseDto> clientes = createMultipleMockClientes();
        List<ProdutoResponseDto> produtos = createMultipleMockProdutos();

        when(clienteClient.getClientes()).thenReturn(clientes);
        when(produtoClient.getProdutos()).thenReturn(produtos);

        // When
        List<CompraAgregada> result = dataAggregationService.getComprasAgregadas();

        // Then
        assertThat(result).hasSize(3); // 2 compras do primeiro cliente + 1 do segundo
        assertThat(result.stream().map(CompraAgregada::getNomeCliente).distinct()).hasSize(2);
    }

    private List<ClienteResponseDto> createMockClientes() {
        ClienteResponseDto cliente = new ClienteResponseDto();
        cliente.setNome("João Silva");
        cliente.setCpf("123.456.789-00");

        ClienteCompraResponseDto compra = new ClienteCompraResponseDto();
        compra.setCodigo(1L);
        compra.setQuantidade(2);

        cliente.setCompras(List.of(compra));
        return List.of(cliente);
    }

    private List<ProdutoResponseDto> createMockProdutos() {
        ProdutoResponseDto produto = new ProdutoResponseDto();
        produto.setCodigo(1L);
        produto.setTipoVinho("Tinto");
        produto.setPreco(BigDecimal.valueOf(50.00));
        produto.setSafra("2020");
        produto.setAnoCompra(2023);

        return List.of(produto);
    }

    private List<ClienteResponseDto> createMultipleMockClientes() {
        // Cliente 1
        ClienteResponseDto cliente1 = new ClienteResponseDto();
        cliente1.setNome("João Silva");
        cliente1.setCpf("123.456.789-00");

        ClienteCompraResponseDto compra1 = new ClienteCompraResponseDto();
        compra1.setCodigo(1L);
        compra1.setQuantidade(2);

        ClienteCompraResponseDto compra2 = new ClienteCompraResponseDto();
        compra2.setCodigo(2L);
        compra2.setQuantidade(1);

        cliente1.setCompras(List.of(compra1, compra2));

        // Cliente 2
        ClienteResponseDto cliente2 = new ClienteResponseDto();
        cliente2.setNome("Maria Santos");
        cliente2.setCpf("987.654.321-00");

        ClienteCompraResponseDto compra3 = new ClienteCompraResponseDto();
        compra3.setCodigo(1L);
        compra3.setQuantidade(3);

        cliente2.setCompras(List.of(compra3));

        return List.of(cliente1, cliente2);
    }

    private List<ProdutoResponseDto> createMultipleMockProdutos() {
        ProdutoResponseDto produto1 = new ProdutoResponseDto();
        produto1.setCodigo(1L);
        produto1.setTipoVinho("Tinto");
        produto1.setPreco(BigDecimal.valueOf(50.00));
        produto1.setSafra("2020");
        produto1.setAnoCompra(2023);

        ProdutoResponseDto produto2 = new ProdutoResponseDto();
        produto2.setCodigo(2L);
        produto2.setTipoVinho("Branco");
        produto2.setPreco(BigDecimal.valueOf(75.00));
        produto2.setSafra("2021");
        produto2.setAnoCompra(2023);

        return List.of(produto1, produto2);
    }
}

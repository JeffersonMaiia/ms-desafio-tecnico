package com.example.msdesafiotecnico.service;

import com.example.msdesafiotecnico.client.ClienteClient;
import com.example.msdesafiotecnico.client.ProdutoClient;
import com.example.msdesafiotecnico.domain.CompraAgregada;
import com.example.msdesafiotecnico.dto.ClienteResponseDto;
import com.example.msdesafiotecnico.dto.ProdutoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class DataAggregationService {

    private final ClienteClient clienteClient;
    private final ProdutoClient produtoClient;

    @Cacheable(value = "compras-agregadas", key = "'all'")
    public List<CompraAgregada> getComprasAgregadas() {
        log.info("Iniciando agregação de dados de compras");

        // Busca paralela de dados externos
        CompletableFuture<List<ClienteResponseDto>> clientesFuture = buscarClientesAsync();
        CompletableFuture<List<ProdutoResponseDto>> produtosFuture = buscarProdutosAsync();

        // Aguarda ambas as operações
        List<ClienteResponseDto> clientes = clientesFuture.join();
        List<ProdutoResponseDto> produtos = produtosFuture.join();

        // Cria índice de produtos para busca O(1)
        Map<Long, ProdutoResponseDto> produtosIndex = produtos.stream()
                .collect(Collectors.toMap(ProdutoResponseDto::getCodigo, Function.identity()));

        // Agrega dados de forma eficiente
        return clientes.parallelStream()
                .flatMap(cliente -> cliente.getCompras().stream()
                        .map(compra -> {
                            ProdutoResponseDto produto = produtosIndex.get(compra.getCodigo());
                            if (produto == null) {
                                log.warn("Produto não encontrado para código: {}", compra.getCodigo());
                                return null;
                            }

                            return CompraAgregada.from(
                                    new CompraAgregada.ClienteData(cliente.getNome(), cliente.getCpf()),
                                    new CompraAgregada.CompraData(compra.getCodigo(), compra.getQuantidade()),
                                    new CompraAgregada.ProdutoData(produto.getCodigo(), produto.getTipoVinho(),
                                            produto.getSafra(), produto.getAnoCompra(), produto.getPreco())
                            );
                        })
                        .filter(java.util.Objects::nonNull))
                .toList();
    }

    @Async("taskExecutor")
    @Cacheable(value = "clientes", key = "'all'")
    public CompletableFuture<List<ClienteResponseDto>> buscarClientesAsync() {
        log.debug("Buscando clientes de forma assíncrona");
        try {
            List<ClienteResponseDto> clientes = clienteClient.getClientes();
            return CompletableFuture.completedFuture(clientes);
        } catch (Exception ex) {
            log.error("Erro ao buscar clientes", ex);
            return CompletableFuture.completedFuture(List.of());
        }
    }

    @Async("taskExecutor")
    @Cacheable(value = "produtos", key = "'all'")
    public CompletableFuture<List<ProdutoResponseDto>> buscarProdutosAsync() {
        log.debug("Buscando produtos de forma assíncrona");
        try {
            List<ProdutoResponseDto> produtos = produtoClient.getProdutos();
            return CompletableFuture.completedFuture(produtos);
        } catch (Exception ex) {
            log.error("Erro ao buscar produtos", ex);
            return CompletableFuture.completedFuture(List.of());
        }
    }
}

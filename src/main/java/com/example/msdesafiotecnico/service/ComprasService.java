package com.example.msdesafiotecnico.service;

import com.example.msdesafiotecnico.client.ClienteClient;
import com.example.msdesafiotecnico.client.ProdutoClient;
import com.example.msdesafiotecnico.dto.ClienteCompraResponseDto;
import com.example.msdesafiotecnico.dto.ClienteResponseDto;
import com.example.msdesafiotecnico.dto.ProdutoResponseDto;
import com.example.msdesafiotecnico.dto.ResumoComprasClientResponseDto;
import com.example.msdesafiotecnico.exception.NoContentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ComprasService {

    private final ClienteClient clienteClient;
    private final ProdutoClient produtoClient;

    public List<ResumoComprasClientResponseDto> findAllCompras() {
        List<ClienteResponseDto> clientes = clienteClient.getClientes();
        List<ProdutoResponseDto> produtos = produtoClient.getProdutos();

        if(CollectionUtils.isEmpty(clientes) || CollectionUtils.isEmpty(produtos)) {
            throw new NoContentException("Não foi possível encontrar clientes e produtos.");
        }

        return clientes.stream()
                .map(cliente -> cliente.getCompras().stream()
                        .map(compra -> getResumoCompras(cliente, compra, produtos))
                        .reduce(ComprasService::getResumoQtdTotalCompras)
                        .get())
                .sorted(Comparator.comparing(ResumoComprasClientResponseDto::getValorTotalCompras))
                .toList();
    }

    public ResumoComprasClientResponseDto findMaiorCompra(Integer anoCompra) {
        List<ClienteResponseDto> clientes = clienteClient.getClientes();
        List<ProdutoResponseDto> produtos = produtoClient.getProdutos();

        if(CollectionUtils.isEmpty(clientes) || CollectionUtils.isEmpty(produtos)) {
            throw new NoContentException("Não foi possível encontrar clientes e produtos.");
        }

        return clientes.stream()
                .map(cliente -> cliente.getCompras().stream()
                        .map(compra -> getResumoComprasByAno(anoCompra, cliente, compra, produtos))
                        .reduce(ComprasService::getResumoQtdTotalCompras)
                        .get())
                .sorted(Comparator.comparing(ResumoComprasClientResponseDto::getValorTotalCompras).reversed())
                .filter(resumo -> resumo.getValorTotalCompras().compareTo(BigDecimal.ZERO) > 0)
                .findFirst()
                .orElseThrow(() -> new NoContentException("""
                        Não foi possível encontrar a maior compra realizada no ano de %d.
                        """.formatted(anoCompra)));
    }

    private static ResumoComprasClientResponseDto getResumoQtdTotalCompras(ResumoComprasClientResponseDto resumo1, ResumoComprasClientResponseDto resumo2) {
        resumo1.setQtdTotalCompras(resumo1.getQtdTotalCompras() + resumo2.getQtdTotalCompras());
        resumo1.setValorTotalCompras(resumo1.getValorTotalCompras().add(resumo2.getValorTotalCompras()));
        return resumo1;
    }

    private static ResumoComprasClientResponseDto getResumoCompras(ClienteResponseDto cliente,
                                                                   ClienteCompraResponseDto compra,
                                                                   List<ProdutoResponseDto> produtos) {

        ResumoComprasClientResponseDto resumo = new ResumoComprasClientResponseDto();
        resumo.setNomeCliente(cliente.getNome());
        resumo.setCpfCliente(cliente.getCpf());
        resumo.setQtdTotalCompras(compra.getQuantidade());

        resumo.setValorTotalCompras(produtos.stream()
                .filter(produto -> produto.getCodigo().equals(compra.getCodigo()))
                .findFirst()
                .map(produto -> produto.getPreco().multiply(BigDecimal.valueOf(compra.getQuantidade()))
                                .setScale(2, RoundingMode.HALF_UP)
                )
                .orElse(BigDecimal.ZERO));

        return resumo;
    }

    private static ResumoComprasClientResponseDto getResumoComprasByAno(Integer anoCompra,
                                                                        ClienteResponseDto cliente,
                                                                        ClienteCompraResponseDto compra,
                                                                        List<ProdutoResponseDto> produtos) {
        ResumoComprasClientResponseDto resumo = new ResumoComprasClientResponseDto();
        resumo.setNomeCliente(cliente.getNome());
        resumo.setCpfCliente(cliente.getCpf());
        resumo.setQtdTotalCompras(compra.getQuantidade());

        resumo.setValorTotalCompras(produtos.stream()
                .filter(produto -> produto.getAnoCompra().equals(anoCompra))
                .filter(produto -> produto.getCodigo().equals(compra.getCodigo()))
                .findFirst()
                .map(produto -> produto.getPreco().multiply(BigDecimal.valueOf(compra.getQuantidade()))
                                .setScale(2, RoundingMode.HALF_UP)
                )
                .orElse(BigDecimal.ZERO));

        return resumo;
    }
}

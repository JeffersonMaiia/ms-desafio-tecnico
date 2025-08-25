package com.example.msdesafiotecnico.service;

import com.example.msdesafiotecnico.domain.CompraAgregada;
import com.example.msdesafiotecnico.dto.RecomendacaoProdutoResponseDto;
import com.example.msdesafiotecnico.dto.ResumoComprasClientResponseDto;
import com.example.msdesafiotecnico.exception.NoContentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ComprasService {

    private final DataAggregationService dataAggregationService;

    @Cacheable(value = "compras-ordenadas", key = "'all'")
    public List<ResumoComprasClientResponseDto> findAllCompras() {
        log.info("Buscando todas as compras ordenadas por valor");

        List<CompraAgregada> compras = dataAggregationService.getComprasAgregadas();
        validarDados(compras);

        // Agrupa por cliente e calcula totais de forma eficiente
        return compras.parallelStream()
                .collect(Collectors.groupingBy(
                        CompraAgregada::getCpfCliente,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                this::calcularResumoCliente
                        )
                ))
                .values()
                .stream()
                .sorted(Comparator.comparing(ResumoComprasClientResponseDto::getValorTotalCompras))
                .toList();
    }

    @Cacheable(value = "maior-compra", key = "#anoCompra")
    public ResumoComprasClientResponseDto findMaiorCompra(Integer anoCompra) {
        log.info("Buscando maior compra do ano: {}", anoCompra);

        List<CompraAgregada> compras = dataAggregationService.getComprasAgregadas();
        validarDados(compras);

        return compras.parallelStream()
                .filter(compra -> anoCompra.equals(compra.getAnoCompra()))
                .collect(Collectors.groupingBy(
                        CompraAgregada::getCpfCliente,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                this::calcularResumoCliente
                        )
                ))
                .values()
                .stream()
                .max(Comparator.comparing(ResumoComprasClientResponseDto::getValorTotalCompras))
                .orElseThrow(() -> new NoContentException(
                        "Não foi possível encontrar a maior compra realizada no ano de %d.".formatted(anoCompra)));
    }

    @Cacheable(value = "clientes-fieis", key = "'top3'")
    public List<ResumoComprasClientResponseDto> findClientesFieis() {
        log.info("Buscando top 3 clientes mais fiéis");

        List<CompraAgregada> compras = dataAggregationService.getComprasAgregadas();
        validarDados(compras);

        // Algoritmo otimizado para clientes fiéis: considera frequência + valor
        return compras.parallelStream()
                .collect(Collectors.groupingBy(
                        CompraAgregada::getCpfCliente,
                        Collectors.collectingAndThen(Collectors.toList(), this::calcularFidelidadeCliente)
                ))
                .values()
                .stream()
                .sorted(Comparator
                        .comparing(ResumoComprasClientResponseDto::getQtdTotalCompras)
                        .thenComparing(ResumoComprasClientResponseDto::getValorTotalCompras)
                        .reversed())
                .limit(3)
                .toList();
    }

    @Cacheable(value = "recomendacoes", key = "'vinhos'")
    public List<RecomendacaoProdutoResponseDto> recomendacaoProdutos() {
        log.info("Gerando recomendações de vinhos");

        List<CompraAgregada> compras = dataAggregationService.getComprasAgregadas();
        validarDados(compras);

        // Algoritmo de recomendação baseado em preferências históricas
        return compras.parallelStream()
                .collect(Collectors.groupingBy(
                        CompraAgregada::getCpfCliente,
                        Collectors.groupingBy(
                                CompraAgregada::getTipoVinho,
                                Collectors.summingInt(CompraAgregada::getQuantidade)
                        )
                ))
                .entrySet()
                .parallelStream()
                .map(entry -> {
                    String cpf = entry.getKey();
                    String tipoVinhoPreferido = entry.getValue().entrySet().stream()
                            .max(Map.Entry.comparingByValue())
                            .map(Map.Entry::getKey)
                            .orElse("Desconhecido");

                    // Busca dados do cliente de forma otimizada
                    return compras.stream()
                            .filter(c -> cpf.equals(c.getCpfCliente()))
                            .findFirst()
                            .map(cliente -> {
                                RecomendacaoProdutoResponseDto recomendacao = new RecomendacaoProdutoResponseDto();
                                recomendacao.setNomeCliente(cliente.getNomeCliente());
                                recomendacao.setCpfCliente(cliente.getCpfCliente());
                                recomendacao.setTipoVinho(tipoVinhoPreferido);
                                return recomendacao;
                            })
                            .orElse(null);
                })
                .filter(Objects::nonNull)
                .toList();
    }

    private ResumoComprasClientResponseDto calcularResumoCliente(List<CompraAgregada> comprasCliente) {
        if (comprasCliente.isEmpty()) {
            return new ResumoComprasClientResponseDto();
        }

        CompraAgregada primeira = comprasCliente.getFirst();
        ResumoComprasClientResponseDto resumo = new ResumoComprasClientResponseDto();
        resumo.setNomeCliente(primeira.getNomeCliente());
        resumo.setCpfCliente(primeira.getCpfCliente());

        resumo.setQtdTotalCompras(comprasCliente.stream()
                .mapToInt(CompraAgregada::getQuantidade)
                .sum());

        resumo.setValorTotalCompras(comprasCliente.stream()
                .map(CompraAgregada::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        return resumo;
    }

    private ResumoComprasClientResponseDto calcularFidelidadeCliente(List<CompraAgregada> comprasCliente) {
        ResumoComprasClientResponseDto resumo = calcularResumoCliente(comprasCliente);

        // Calcula score de fidelidade baseado em recorrência e valor
        long comprasRecorrentes = comprasCliente.stream()
                .collect(Collectors.groupingBy(CompraAgregada::getTipoVinho, Collectors.counting()))
                .values()
                .stream()
                .filter(count -> count > 1)
                .count();

        // Aplica peso para compras recorrentes
        resumo.setQtdTotalCompras((int) (resumo.getQtdTotalCompras() + comprasRecorrentes * 2));

        return resumo;
    }

    private void validarDados(List<CompraAgregada> compras) {
        if (CollectionUtils.isEmpty(compras)) {
            throw new NoContentException("Não foi possível encontrar dados de compras.");
        }
    }
}

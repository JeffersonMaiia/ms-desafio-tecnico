package com.example.msdesafiotecnico.mapper;

import com.example.msdesafiotecnico.domain.CompraAgregada;
import com.example.msdesafiotecnico.dto.ResumoComprasClientResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CompraMapper {

    CompraMapper INSTANCE = Mappers.getMapper(CompraMapper.class);

    @Mapping(source = "nomeCliente", target = "nomeCliente")
    @Mapping(source = "cpfCliente", target = "cpfCliente")
    @Mapping(target = "qtdTotalCompras", ignore = true)
    @Mapping(target = "valorTotalCompras", ignore = true)
    ResumoComprasClientResponseDto toResumoDto(CompraAgregada compra);

    default ResumoComprasClientResponseDto toResumoComTotais(List<CompraAgregada> compras) {
        if (compras == null || compras.isEmpty()) {
            return new ResumoComprasClientResponseDto();
        }

        CompraAgregada primeira = compras.get(0);
        ResumoComprasClientResponseDto resumo = new ResumoComprasClientResponseDto();
        resumo.setNomeCliente(primeira.getNomeCliente());
        resumo.setCpfCliente(primeira.getCpfCliente());

        // Cálculos otimizados
        int totalQuantidade = compras.stream()
                .mapToInt(CompraAgregada::getQuantidade)
                .sum();

        BigDecimal valorTotal = compras.stream()
                .map(CompraAgregada::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        resumo.setQtdTotalCompras(totalQuantidade);
        resumo.setValorTotalCompras(valorTotal);

        return resumo;
    }
}

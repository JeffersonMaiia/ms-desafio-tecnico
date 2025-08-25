package com.example.msdesafiotecnico.controller;

import com.example.msdesafiotecnico.controller.api.ComprasControllerApi;
import com.example.msdesafiotecnico.dto.RecomendacaoProdutoResponseDto;
import com.example.msdesafiotecnico.dto.ResumoComprasClientResponseDto;
import com.example.msdesafiotecnico.service.ComprasService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1")
@Validated
public class ComprasController implements ComprasControllerApi {

    private final ComprasService comprasService;

    @GetMapping("/compras")
    public ResponseEntity<List<ResumoComprasClientResponseDto>> findAllCompras() {
        log.info("Requisição recebida para listar todas as compras");
        List<ResumoComprasClientResponseDto> compras = comprasService.findAllCompras();
        log.info("Retornando {} compras ordenadas", compras.size());
        return ResponseEntity.ok(compras);
    }

    @GetMapping("/maior_compra/{anoCompra}")
    public ResponseEntity<ResumoComprasClientResponseDto> findMaiorCompra(@PathVariable Integer anoCompra) {
        log.info("Requisição recebida para maior compra do ano: {}", anoCompra);
        ResumoComprasClientResponseDto maiorCompra = comprasService.findMaiorCompra(anoCompra);
        return ResponseEntity.ok(maiorCompra);
    }

    @GetMapping("/clientes_fieis")
    public ResponseEntity<List<ResumoComprasClientResponseDto>> findClientesFieis() {
        log.info("Requisição recebida para top 3 clientes mais fiéis");
        List<ResumoComprasClientResponseDto> clientesFieis = comprasService.findClientesFieis();
        log.info("Retornando {} clientes fiéis", clientesFieis.size());
        return ResponseEntity.ok(clientesFieis);
    }

    @GetMapping("/recomendacao/cliente/tipo")
    public ResponseEntity<List<RecomendacaoProdutoResponseDto>> recomendacaoProdutos() {
        log.info("Requisição recebida para recomendações de vinhos");
        List<RecomendacaoProdutoResponseDto> recomendacoes = comprasService.recomendacaoProdutos();
        log.info("Retornando {} recomendações", recomendacoes.size());
        return ResponseEntity.ok(recomendacoes);
    }
}

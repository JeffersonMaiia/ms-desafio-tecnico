package com.example.msdesafiotecnico.controller;

import com.example.msdesafiotecnico.dto.ResumoComprasClientResponseDto;
import com.example.msdesafiotecnico.service.ComprasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1")
public class ComprasController {

    private final ComprasService comprasService;

    @GetMapping("/compras")
    public ResponseEntity<List<ResumoComprasClientResponseDto>> findAllCompras() {
        return ResponseEntity.ok(comprasService.findAllCompras());
    }

    @GetMapping("/maior_compra/{anoCompra}")
    public ResponseEntity<ResumoComprasClientResponseDto> findMaiorCompra(@PathVariable Integer anoCompra) {
        return ResponseEntity.ok(comprasService.findMaiorCompra(anoCompra));
    }

    @GetMapping("/clientes_fieis")
    public ResponseEntity<List<ResumoComprasClientResponseDto>> findClientesFieis() {
        return ResponseEntity.ok(comprasService.findClientesFieis());
    }

}

package com.example.msdesafiotecnico.controller;

import com.example.msdesafiotecnico.dto.ResumoComprasClientResponseDto;
import com.example.msdesafiotecnico.service.ComprasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/compras")
public class ComprasController {

    private final ComprasService comprasService;

    @GetMapping
    public ResponseEntity<List<ResumoComprasClientResponseDto>> findAllCompras() {
        return ResponseEntity.ok(comprasService.findAllCompras());
    }

}

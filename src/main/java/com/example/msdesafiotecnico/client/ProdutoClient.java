package com.example.msdesafiotecnico.client;

import com.example.msdesafiotecnico.dto.ProdutoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "produto-ms", url = "${external-ms.url}")
public interface ProdutoClient {

    @GetMapping("/produtos-mnboX5IPl6VgG390FECTKqHsD9SkLS.json")
    List<ProdutoResponseDto> getProdutos();
}

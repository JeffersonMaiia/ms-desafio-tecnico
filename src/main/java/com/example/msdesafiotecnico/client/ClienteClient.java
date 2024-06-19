package com.example.msdesafiotecnico.client;

import com.example.msdesafiotecnico.dto.ClienteResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "client-ms" , url = "${external-ms.url}")
public interface ClienteClient {

    @GetMapping("/clientes-Vz1U6aR3GTsjb3W8BRJhcNKmA81pVh.json")
    List<ClienteResponseDto> getClientes();
}

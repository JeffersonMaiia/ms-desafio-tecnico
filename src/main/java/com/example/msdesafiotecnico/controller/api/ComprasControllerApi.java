package com.example.msdesafiotecnico.controller.api;

import com.example.msdesafiotecnico.dto.RecomendacaoProdutoResponseDto;
import com.example.msdesafiotecnico.dto.ResumoComprasClientResponseDto;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@OpenAPIDefinition(info = @Info(title = "Compras API", version = "1.0", description = "API para gerenciamento de compras"))
public interface ComprasControllerApi {

    @Operation(summary = "Retornar uma lista das compras ordenadas de forma crescente por valor, deve conter o nome dos clientes, cpf dos clientes, dado dos produtos, quantidade das compras e valores totais de cada compra.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Compras obtidas"),
            @ApiResponse(responseCode = "204", description = "Compras não encontradas"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    ResponseEntity<List<ResumoComprasClientResponseDto>> findAllCompras();

    @Operation(summary = "Retornar a maior compra do ano informando os dados da compra disponibilizados, deve ter o nome do cliente, cpf do cliente, dado do produto, quantidade da compra e seu valor total.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Maior compra obtidas"),
            @ApiResponse(responseCode = "204", description = "Maior compra não encontradas"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    ResponseEntity<ResumoComprasClientResponseDto> findMaiorCompra(@PathVariable Integer anoCompra);

    @Operation(summary = "Retornar o Top 3 clientes mais fieis, clientes que possuem mais compras recorrentes com maiores valores.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Top 3 clientes mais fieis obtidas"),
            @ApiResponse(responseCode = "204", description = "Top 3 clientes mais fieis não encontradas"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    ResponseEntity<List<ResumoComprasClientResponseDto>> findClientesFieis();

    @Operation(summary = "Retornar uma recomendação de vinho baseado nos tipos de vinho que o cliente mais compra.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recomendações de vinhos obtidas"),
            @ApiResponse(responseCode = "204", description = "Recomendações de vinhos não encontradas"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    ResponseEntity<List<RecomendacaoProdutoResponseDto>> recomendacaoProdutos();
}

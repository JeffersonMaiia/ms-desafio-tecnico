package com.example.msdesafiotecnico.config;

import com.example.msdesafiotecnico.exception.IntegrationServiceRestException;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FeignConfig {

    @Bean
    ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            log.error("Erro ao realizar a integração. Método: {} Status: {}, Request: {}",
                    methodKey, response.status(), response.request());

            return new IntegrationServiceRestException("Erro ao realizar a integração: " + methodKey);
        };
    }
}

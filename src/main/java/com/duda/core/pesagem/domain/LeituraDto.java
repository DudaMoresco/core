package com.duda.core.pesagem.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LeituraDto(
        @NotNull(message = "ID da balança é obrigatório")
        Long id,

        @NotBlank(message = "Placa do caminhão é obrigatória")
        String plate,

        @NotNull(message = "Peso é obrigatório")
        @Positive(message = "Peso deve ser positivo")
        BigDecimal weight,

        LocalDateTime timestamp
) {
    public LeituraDto {

        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}
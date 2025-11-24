package com.duda.core.balanca;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MedicaoBalancaCreateDto {

    @NotNull(message = "ID da balança é obrigatório")
    private Long id;

    @NotBlank(message = "Placa do caminhão é obrigatória")
    private String plate;

    @NotNull(message = "Peso é obrigatório")
    @Positive(message = "Peso deve ser positivo")
    private BigDecimal weight;
}

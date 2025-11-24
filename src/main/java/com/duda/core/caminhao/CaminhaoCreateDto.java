package com.serasa.core.caminhao;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CaminhaoCreateDto {

    @NotBlank(message = "Placa é obrigatória")
    @Pattern(regexp = "^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$|^[A-Z]{3}-[0-9]{4}$",
            message = "Placa deve estar no formato ABC1234 ou ABC-1234")
    @Column(name = "placa", nullable = false, unique = true, length = 8)
    private String placa;

    @NotNull(message = "Tara é obrigatória")
    @PositiveOrZero(message = "Tara deve ser positiva ou zero")
    @Column(name = "tara", nullable = false, precision = 10, scale = 3)
    private BigDecimal tara;

}

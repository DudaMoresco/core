package com.duda.core.estoque.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EstoqueDocaGraoUpdateDto {

    @PositiveOrZero(message = "Quantidade máxima deve ser zero ou positiva")
    private BigDecimal qtdMax;

    @PositiveOrZero(message = "Quantidade atual deve ser zero ou positiva")
    private BigDecimal qtdAtual;
}
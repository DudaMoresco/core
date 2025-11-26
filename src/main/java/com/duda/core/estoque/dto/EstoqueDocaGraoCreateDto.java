package com.duda.core.estoque.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EstoqueDocaGraoCreateDto {

    @NotNull(message = "ID da doca é obrigatório")
    private Long idDoca;

    @NotNull(message = "ID do grão é obrigatório")
    private Long idGrao;

    @NotNull(message = "Quantidade máxima é obrigatória")
    @PositiveOrZero(message = "Quantidade máxima deve ser zero ou positiva")
    private BigDecimal qtdMax;

    @NotNull(message = "Quantidade atual é obrigatória")
    @PositiveOrZero(message = "Quantidade atual deve ser zero ou positiva")
    private BigDecimal qtdAtual;
}
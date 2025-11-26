package com.duda.core.balanca.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BalancaCreateDto {

    @NotNull(message = "ID da doca é obrigatório")
    private Long docaId;
}
package com.serasa.core.balanca;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BalancaResponseDto {

    private Long id;
    private Long docaId;

    public static BalancaResponseDto from(BalancaEntity entity) {
        return new BalancaResponseDto(
                entity.getId(),
                entity.getDoca().getId()
        );
    }
}
package com.duda.core.grao;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class GraoResponseDto {

    private Long id;
    private String nome;
    private BigDecimal precoCompraPorTonelada;

    public static GraoResponseDto from(GraoEntity entity) {
        return new GraoResponseDto(
                entity.getId(),
                entity.getNome(),
                entity.getPrecoCompraPorTonelada()
        );
    }
}

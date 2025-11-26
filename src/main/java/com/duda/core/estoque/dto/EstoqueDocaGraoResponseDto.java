package com.duda.core.estoque.dto;

import com.duda.core.estoque.entity.EstoqueDocaGraoEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class EstoqueDocaGraoResponseDto {

    private Long id;
    private Long idDoca;
    private Long idGrao;
    private String nomeGrao;
    private BigDecimal qtdMax;
    private BigDecimal qtdAtual;

    public static EstoqueDocaGraoResponseDto from(EstoqueDocaGraoEntity entity) {
        return new EstoqueDocaGraoResponseDto(
                entity.getId(),
                entity.getDoca().getId(),
                entity.getGrao().getId(),
                entity.getGrao().getNome(),
                entity.getQtdMax(),
                entity.getQtdAtual()
        );
    }
}
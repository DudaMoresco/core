package com.duda.core.filial.dto;

import com.duda.core.filial.entity.FilialEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FilialResponseDto {

    private Long id;
    private String nome;
    private String cnpj;

    public static FilialResponseDto from(FilialEntity entity) {
        return new FilialResponseDto(
                entity.getId(),
                entity.getNome(),
                entity.getCnpj()
        );
    }
}

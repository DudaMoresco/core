package com.duda.core.doca;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DocaResponseDto {

    private Long id;

    public static DocaResponseDto from(DocaEntity entity) {
        return new DocaResponseDto(entity.getId());
    }
}
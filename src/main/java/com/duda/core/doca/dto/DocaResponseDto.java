package com.duda.core.doca.dto;

import com.duda.core.doca.entity.DocaEntity;
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
package com.serasa.core.caminhao;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class CaminhaoResponseDto {

    private Long id;
    private String place;
    private BigDecimal tara;

    public static CaminhaoResponseDto from(CaminhaoEntity entity) {
        return new CaminhaoResponseDto(entity.getId(), entity.getPlaca(), entity.getTara());
    }

}

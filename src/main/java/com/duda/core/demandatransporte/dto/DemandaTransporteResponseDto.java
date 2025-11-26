package com.duda.core.demandatransporte.dto;

import com.duda.core.demandatransporte.entity.DemandaTransporteEntity;
import com.duda.core.demandatransporte.enumeration.StatusDemandaTransporte;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DemandaTransporteResponseDto {

    private Long id;
    private Long idCaminhao;
    private Long idGrao;
    private Long idFilial;
    private StatusDemandaTransporte status;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    public static DemandaTransporteResponseDto from(DemandaTransporteEntity entity) {
        return new DemandaTransporteResponseDto(
                entity.getId(),
                entity.getIdCaminhao(),
                entity.getIdGrao(),
                entity.getIdFilial(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getUpdatedAt(),
                entity.getUpdatedBy()
        );
    }
}
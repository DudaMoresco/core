package com.duda.core.demandatransporte.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DemandaTransporteCreateDTO {

    @NotNull(message = "ID do Caminhão é obrigatório")
    private Long idCaminhao;

    @NotNull(message = "ID do Grão é obrigatório")
    private Long idGrao;

    @NotNull(message = "ID da Filial é obrigatório")
    private Long idFilial;
}

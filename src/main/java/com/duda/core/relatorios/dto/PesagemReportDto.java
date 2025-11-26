package com.duda.core.relatorios.dto;

import com.duda.core.relatorios.repository.PesagemReportProjection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PesagemReportDto(
    Long pesagemId,
    String nomeFilial,
    String cnpjFilial,
    String placaCaminhao,
    String nomeGrao,
    BigDecimal pesoBruto,
    BigDecimal tara,
    BigDecimal pesoLiquido,
    BigDecimal custoCompra,
    LocalDateTime dataPesagem,
    String criadoPor
) {
    public static PesagemReportDto fromProjection(PesagemReportProjection projection) {
        return new PesagemReportDto(
                projection.getPesagemId(),
                projection.getNomeFilial(),
                projection.getCnpjFilial(),
                projection.getPlacaCaminhao(),
                projection.getNomeGrao(),
                projection.getPesoBruto(),
                projection.getTara(),
                projection.getPesoLiquido(),
                projection.getCusto(),
                projection.getDataPesagem(),
                projection.getCriadoPor()
        );
    }
}
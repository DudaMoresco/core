package com.duda.core.relatorios.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PesagemReportProjection {
    Long getPesagemId();
    String getNomeFilial();
    String getCnpjFilial();
    String getPlacaCaminhao();
    String getNomeGrao();
    BigDecimal getPesoBruto();
    BigDecimal getTara();
    BigDecimal getPesoLiquido();
    BigDecimal getCusto();
    LocalDateTime getDataPesagem();
    String getCriadoPor();
}

package com.duda.core.pesagem.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PesoEstabilizadoDTO(
        Long idBalanca,
        String placa,
        BigDecimal pesoBruto,
        LocalDateTime timestamp,
        int readingsCount,
        BigDecimal stdDeviation
) {}
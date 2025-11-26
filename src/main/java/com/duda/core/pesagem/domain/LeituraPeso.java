package com.duda.core.pesagem.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LeituraPeso {

    private final BigDecimal peso;

    private final LocalDateTime timestamp;

    public boolean ehAnteriorA(LocalDateTime momento) {
        return this.timestamp.isBefore(momento);
    }

    public long segundosAte(LeituraPeso outraLeitura) {
        return java.time.Duration.between(this.timestamp, outraLeitura.getTimestamp()).getSeconds();
    }
}

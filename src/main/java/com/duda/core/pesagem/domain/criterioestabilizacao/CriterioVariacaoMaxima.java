package com.duda.core.pesagem.domain.criterioestabilizacao;

import com.duda.core.pesagem.domain.EstadoBalanca;
import com.duda.core.pesagem.domain.LeituraPeso;
import com.duda.core.shared.utils.CalculadoraEstatistica;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class CriterioVariacaoMaxima implements CriterioEstabilizacao {

    @Override
    public boolean avaliar(EstadoBalanca estado) {
        List<BigDecimal> pesos = estado.getLeituras().stream()
                .map(LeituraPeso::getPeso)
                .toList();

        BigDecimal media = CalculadoraEstatistica.calcularMedia(pesos);
        BigDecimal max = pesos.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal min = pesos.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);

        BigDecimal variacao = max.subtract(min)
                .divide(media, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        double variacaoMaximaPercentual = 0.5;
        return variacao.doubleValue() <= variacaoMaximaPercentual;
    }
}
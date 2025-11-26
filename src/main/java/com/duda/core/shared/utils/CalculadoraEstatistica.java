package com.duda.core.shared.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CalculadoraEstatistica {

    public static BigDecimal calcularMedia(List<BigDecimal> valores) {
        return valores.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(valores.size()), 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calcularMediana(List<BigDecimal> valores) {
        List<BigDecimal> ordenados = valores.stream().sorted().toList();
        int size = ordenados.size();
        int mid = size / 2;

        if (size % 2 == 0) {
            return ordenados.get(mid - 1)
                    .add(ordenados.get(mid))
                    .divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
        }
        return ordenados.get(mid);
    }

    public static BigDecimal calcularDesvioPadrao(List<BigDecimal> valores) {
        BigDecimal media = calcularMedia(valores);

        BigDecimal somaQuadrados = valores.stream()
                .map(v -> v.subtract(media).pow(2))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal variancia = somaQuadrados
                .divide(BigDecimal.valueOf(valores.size()), 4, RoundingMode.HALF_UP);

        return BigDecimal.valueOf(Math.sqrt(variancia.doubleValue()));
    }
}
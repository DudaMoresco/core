package com.duda.core.relatorios.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CustoReportDto(
    Long pesagemId,
    String nomeFilial,
    String cnpjFilial,
    String placaCaminhao,
    String nomeGrao,
    BigDecimal pesoLiquido,
    BigDecimal custoCompra,
    BigDecimal margemLucro,
    BigDecimal precoVenda,
    BigDecimal lucro,
    Integer qtdDisponivelEstoque,
    LocalDateTime dataPesagem,
    String criadoPor
) {
    public static CustoReportDto fromPesagemAndEstoque(
        Long pesagemId,
        String nomeFilial,
        String cnpjFilial,
        String placaCaminhao,
        String nomeGrao,
        BigDecimal pesoBruto,
        BigDecimal tara,
        BigDecimal precoCompraPorTonelada,
        Integer qtdDisponivelEstoque,
        LocalDateTime dataPesagem,
        String criadoPor
    ) {
        BigDecimal pesoLiquido = pesoBruto.subtract(tara);
        BigDecimal custoCompra = calcularCustoCompra(pesoLiquido, precoCompraPorTonelada);
        BigDecimal margemLucro = calcularMargemLucro(qtdDisponivelEstoque);
        BigDecimal precoVenda = custoCompra.multiply(BigDecimal.ONE.add(margemLucro));
        BigDecimal lucro = precoVenda.subtract(custoCompra);
        
        return new CustoReportDto(
            pesagemId,
            nomeFilial,
            cnpjFilial,
            placaCaminhao,
            nomeGrao,
            pesoLiquido,
            custoCompra,
            margemLucro,
            precoVenda,
            lucro,
            qtdDisponivelEstoque,
            dataPesagem,
            criadoPor
        );
    }
    
    private static BigDecimal calcularCustoCompra(BigDecimal pesoLiquido, BigDecimal precoCompraPorTonelada) {
        // Converte peso de kg para toneladas e multiplica pelo preço por tonelada
        return pesoLiquido.divide(BigDecimal.valueOf(1000))
                         .multiply(precoCompraPorTonelada);
    }
    
    private static BigDecimal calcularMargemLucro(Integer qtdDisponivelEstoque) {
        // Margem inversamente proporcional ao estoque disponível
        // Estoque alto (≥100) = margem mínima 5%
        // Estoque médio (50-99) = margem média 12.5%
        // Estoque baixo (<50) = margem máxima 20%
        
        if (qtdDisponivelEstoque >= 100) {
            return BigDecimal.valueOf(0.05); // 5%
        } else if (qtdDisponivelEstoque >= 50) {
            // Interpolação linear entre 5% e 20%
            double percentual = 50.0 / qtdDisponivelEstoque;
            double margem = 0.05 + (0.15 * (percentual - 0.5) * 2);
            return BigDecimal.valueOf(Math.min(0.20, Math.max(0.05, margem)));
        } else {
            return BigDecimal.valueOf(0.20); // 20%
        }
    }
}
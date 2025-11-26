package com.duda.core.pesagem.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;

@Data
public class EstadoBalanca {

    private Long id;
    private String placa;
    private final LinkedList<LeituraPeso> leituras = new LinkedList<>();
    private BigDecimal ultimoPesoEstabilizado;
    private LocalDateTime ultimaEstabilizacao;

    public void adicionarLeitura(LeituraPeso leitura) {
        this.leituras.add(leitura);
    }

    public void removerLeiturasAntigasQue(LocalDateTime limiteTempo) {
        this.leituras.removeIf(leitura -> leitura.getTimestamp().isBefore(limiteTempo));
    }

    public boolean temPesoEstabilizado() {
        return this.ultimoPesoEstabilizado != null;
    }

    public boolean pesoMudou(BigDecimal novoPeso) {
        return this.ultimoPesoEstabilizado == null ||
                !this.ultimoPesoEstabilizado.equals(novoPeso);
    }

    public void registrarEstabilizacao(BigDecimal peso) {
        this.ultimoPesoEstabilizado = peso;
        this.ultimaEstabilizacao = LocalDateTime.now();
    }

    public int quantidadeLeituras() {
        return this.leituras.size();
    }
}